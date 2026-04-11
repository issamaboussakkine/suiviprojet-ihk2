import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { Users, Briefcase, Layers, CreditCard, Loader2 } from 'lucide-react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer
} from 'recharts';

const StatCard = ({ title, value, icon: Icon }) => (
  <div className="bg-theme-card rounded-xl p-5 shadow-sm border border-theme-border flex justify-between items-center transition-colors duration-200">
    <div>
      <p className="text-theme-textSec text-sm m-0 mb-1">{title}</p>
      <h3 className="text-2xl font-bold text-theme-text m-0">{value}</h3>
    </div>
    <div className="w-12 h-12 rounded-xl bg-theme-accent flex items-center justify-center shrink-0">
      <Icon size={22} className="text-white dark:text-theme-card" />
    </div>
  </div>
);

const ProgressBar = ({ taux, label }) => (
  <div className="mb-3">
    <div className="flex justify-between mb-1">
      <span className="text-xs text-theme-textSec">{label}</span>
      <span className="text-xs font-bold text-theme-text">{taux}%</span>
    </div>
    <div className="bg-theme-bg rounded-full h-2 overflow-hidden border border-theme-border">
      <div className="bg-theme-accent h-full rounded-full transition-all duration-500" style={{ width: `${taux}%` }} />
    </div>
  </div>
);

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalEmployes: 0,
    projetsEnCours: 0,
    phasesTermineesNonFacturees: 0,
    chiffreAffaire: 0
  });
  const [projetsParMois, setProjetsParMois] = useState([]);
  const [projetsAvecTaux, setProjetsAvecTaux] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [employesRes, projetsRes, phasesRes] = await Promise.all([
          api.get('/employes').catch(() => ({ data: [] })),
          api.get('/projets').catch(() => ({ data: [] })),
          api.get('/reporting/phases/terminees-non-facturees').catch(() => ({ data: [] }))
        ]);

        const employes = employesRes.data || [];
        const projets = projetsRes.data || [];
        const phasesTerminees = phasesRes.data || [];

        const chiffreAffaire = projets.reduce((sum, p) => sum + (p.montant || 0), 0);
        const projetsEnCours = projets.filter(p => p.statut === 'EN_COURS').length;

        setStats({
          totalEmployes: employes.length,
          projetsEnCours: projetsEnCours,
          phasesTermineesNonFacturees: phasesTerminees.length,
          chiffreAffaire: chiffreAffaire
        });

        const moisMap = {
          0: 'Jan', 1: 'Fev', 2: 'Mar', 3: 'Avr',
          4: 'Mai', 5: 'Juin', 6: 'Juil', 7: 'Aou',
          8: 'Sep', 9: 'Oct', 10: 'Nov', 11: 'Dec'
        };
        const projetsParMoisTemp = {};

        projets.forEach(projet => {
          if (projet.dateDebut) {
            const mois = new Date(projet.dateDebut).getMonth();
            const nomMois = moisMap[mois];
            projetsParMoisTemp[nomMois] = (projetsParMoisTemp[nomMois] || 0) + 1;
          }
        });

        const chartData = Object.keys(projetsParMoisTemp).map(name => ({
          name: name,
          valeur: projetsParMoisTemp[name]
        }));

        if (chartData.length === 0) {
          setProjetsParMois([
            { name: 'Jan', valeur: 0 }, { name: 'Fev', valeur: 0 },
            { name: 'Mar', valeur: 0 }, { name: 'Avr', valeur: 0 }
          ]);
        } else {
          setProjetsParMois(chartData);
        }

        // Utilise le vrai tauxAvancement renvoyé par le backend (maintenant dans le DTO)
        setProjetsAvecTaux(projets.slice(0, 4).map(p => ({ ...p, tauxAvancement: p.tauxAvancement ?? 0 })));

      } catch (error) {
        console.error("Erreur chargement dashboard:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-full min-h-[400px]">
        <Loader2 size={32} className="text-theme-accent animate-spin" />
      </div>
    );
  }

  return (
    <div className="min-h-full">
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-theme-text mb-2">Tableau de bord</h1>
        <p className="text-theme-textSec m-0">Bienvenue sur IHKAPP - Vue globale de l'activité</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-5 mb-6">
        <StatCard title="Employés" value={stats.totalEmployes} icon={Users} />
        <StatCard title="Projets en cours" value={stats.projetsEnCours} icon={Briefcase} />
        <StatCard title="Phases à facturer" value={stats.phasesTermineesNonFacturees} icon={Layers} />
        <StatCard title="Chiffre d'Affaires" value={`${stats.chiffreAffaire.toLocaleString()} MAD`} icon={CreditCard} />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
        <div className="bg-theme-card rounded-xl p-5 shadow-sm border border-theme-border transition-colors duration-200">
          <h3 className="text-lg font-bold text-theme-text mb-4">📊 Projets par mois</h3>
          <ResponsiveContainer width="100%" height={250}>
            <BarChart data={projetsParMois}>
              <CartesianGrid strokeDasharray="3 3" className="stroke-theme-border opacity-50" />
              <XAxis dataKey="name" stroke="var(--color-textSec)" />
              <YAxis stroke="var(--color-textSec)" />
              <Tooltip cursor={{ fill: 'transparent' }} contentStyle={{ borderRadius: '10px', backgroundColor: 'var(--color-bg)', border: '1px solid var(--color-border)', color: 'var(--color-text)' }} />
              <Bar dataKey="valeur" fill="var(--color-accent)" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div className="bg-theme-card rounded-xl p-5 shadow-sm border border-theme-border transition-colors duration-200">
          <h3 className="text-lg font-bold text-theme-text mb-4">🎯 Avancement des projets</h3>
          <div className="flex flex-col gap-4">
            {projetsAvecTaux.map(projet => (
              <div key={projet.id} className="p-4 bg-theme-bg rounded-lg border border-theme-border transition-colors duration-200">
                <div className="flex justify-between items-center mb-2">
                  <h4 className="font-medium text-theme-text m-0">{projet.nom}</h4>
                  <span className="px-3 py-1 rounded-full text-xs font-bold bg-theme-border text-theme-text">
                    {projet.statut === 'TERMINE' ? '✓ Terminé'
                     : projet.statut === 'EN_COURS' ? '🔄 En cours'
                     : projet.statut === 'VALIDE' ? '✅ Validé'
                     : '⏳ En attente'}
                  </span>
                </div>
                <ProgressBar taux={projet.tauxAvancement} label="Avancement" />
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;