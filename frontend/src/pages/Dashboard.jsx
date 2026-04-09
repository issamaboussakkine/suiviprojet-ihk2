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

const StatCard = ({ title, value, icon: Icon, colorClass }) => (
  <div className="bg-slate-800 rounded-xl p-6 border border-slate-700 flex items-center justify-between">
    <div>
      <p className="text-sm text-slate-400 mb-1">{title}</p>
      <h3 className="text-3xl font-bold text-slate-50">{value}</h3>
    </div>
    <div className={`w-12 h-12 rounded-full flex items-center justify-center ${colorClass}`}>
      <Icon size={24} className="text-white" />
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
  const [dernieresPhases, setDernieresPhases] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [employesRes, projetsRes, phasesRes, dashboardRes] = await Promise.all([
          api.get('/employes').catch(() => ({ data: [] })),
          api.get('/projets').catch(() => ({ data: [] })),
          api.get('/reporting/phases/terminees-non-facturees').catch(() => ({ data: [] })),
          api.get('/reporting/tableau-de-bord').catch(() => ({ data: {} }))
        ]);

        const employes = employesRes.data || [];
        const projets = projetsRes.data || [];
        const phasesTerminees = phasesRes.data || [];
        const reporting = dashboardRes.data || {};

        // Calcul du chiffre d'affaires
        const chiffreAffaire = projets.reduce((sum, p) => sum + (p.montant || 0), 0);

        // Projets en cours
        const projetsEnCours = reporting.projetsEnCours || projets.filter(p => p.statut === 'EN_COURS').length;

        setStats({
          totalEmployes: employes.length,
          projetsEnCours: projetsEnCours,
          phasesTermineesNonFacturees: reporting.phasesTermineesNonFacturees || phasesTerminees.length,
          chiffreAffaire: chiffreAffaire
        });

        // === CALCUL DES PROJETS PAR MOIS (VRAIES DONNÉES) ===
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

        // Si aucune donnée, afficher des valeurs par défaut
        if (chartData.length === 0) {
          setProjetsParMois([
            { name: 'Jan', valeur: 0 }, { name: 'Fev', valeur: 0 },
            { name: 'Mar', valeur: 0 }, { name: 'Avr', valeur: 0 }
          ]);
        } else {
          setProjetsParMois(chartData);
        }

        // Phases récentes
        if (phasesTerminees.length > 0) {
          setDernieresPhases(phasesTerminees.slice(0, 5).map(p => ({
            id: p.id,
            titre: p.description || p.libelle || `Phase ${p.id}`
          })));
        } else {
          setDernieresPhases([
            { id: 1, titre: 'Aucune phase terminée' }
          ]);
        }
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
      <div className="flex h-full items-center justify-center">
        <Loader2 className="w-8 h-8 text-blue-500 animate-spin" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-50">Tableau de bord</h1>
        <p className="text-slate-400">Vue globale de l'activité - IHK APP</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard title="Employés" value={stats.totalEmployes} icon={Users} colorClass="bg-blue-500/80" />
        <StatCard title="Projets en cours" value={stats.projetsEnCours} icon={Briefcase} colorClass="bg-emerald-500/80" />
        <StatCard title="Phases terminées/non fact." value={stats.phasesTermineesNonFacturees} icon={Layers} colorClass="bg-amber-500/80" />
        <StatCard title="Chiffre d'Affaires" value={`${stats.chiffreAffaire.toLocaleString()} MAD`} icon={CreditCard} colorClass="bg-indigo-500/80" />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-[400px]">
        <div className="lg:col-span-2 bg-slate-800 rounded-xl p-6 border border-slate-700 h-full flex flex-col">
          <h3 className="text-lg font-semibold text-slate-200 mb-4">Projets par Mois</h3>
          <div className="flex-1">
            {projetsParMois.length > 0 && projetsParMois.some(p => p.valeur > 0) ? (
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={projetsParMois}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                  <XAxis dataKey="name" stroke="#94a3b8" />
                  <YAxis stroke="#94a3b8" />
                  <Tooltip contentStyle={{ backgroundColor: '#1E293B', borderColor: '#334155' }} itemStyle={{ color: '#F8FAFC' }} />
                  <Bar dataKey="valeur" fill="#3B82F6" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <div className="flex items-center justify-center h-full text-slate-400">
                Aucune donnée de projet par mois
              </div>
            )}
          </div>
        </div>

        <div className="bg-slate-800 rounded-xl p-6 border border-slate-700 h-full flex flex-col overflow-hidden">
          <h3 className="text-lg font-semibold text-slate-200 mb-4">Phases Récentes</h3>
          <ul className="space-y-4 flex-1 overflow-y-auto pr-2">
            {dernieresPhases.length === 0 || (dernieresPhases.length === 1 && dernieresPhases[0].titre === 'Aucune phase terminée') ? (
              <li className="text-slate-400 text-center">Aucune phase terminée</li>
            ) : (
              dernieresPhases.map(phase => (
                <li key={phase.id} className="bg-slate-900 border border-slate-700 rounded-lg p-3">
                  <p className="text-sm font-medium text-slate-100">{phase.titre}</p>
                  <p className="text-xs text-slate-500">Non facturée</p>
                </li>
              ))
            )}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;