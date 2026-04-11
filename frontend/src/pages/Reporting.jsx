import { useState, useEffect } from 'react';
import api from '../services/api';
import { Loader2, RefreshCw, BarChart2, Briefcase, FileCheck2, AlertCircle } from 'lucide-react';

export default function Reporting() {
  const [stats, setStats] = useState({
    phasesTermineesNonFacturees: 0,
    phasesFactureesNonPayees: 0,
    projetsEnCours: 0,
    projetsClotures: 0
  });
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    setLoading(true);
    setRefreshing(true);
    try {
      const [termineesRes, factureesRes, enCoursRes, cloturesRes] = await Promise.all([
        api.get('/reporting/phases/terminees-non-facturees').catch(() => ({ data: [] })),
        api.get('/reporting/phases/facturees-non-payees').catch(() => ({ data: [] })),
        api.get('/reporting/projets/en-cours').catch(() => ({ data: [] })),
        api.get('/reporting/projets/clotures').catch(() => ({ data: [] }))
      ]);

      setStats({
        phasesTermineesNonFacturees: termineesRes.data?.length || 0,
        phasesFactureesNonPayees: factureesRes.data?.length || 0,
        projetsEnCours: enCoursRes.data?.length || 0,
        projetsClotures: cloturesRes.data?.length || 0
      });
    } catch (error) {
      console.error('Erreur chargement reporting:', error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  if (loading && !refreshing) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-theme-accent" size={32} /></div>;

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-theme-text mb-1">Rapports & Statistiques</h1>
          <p className="text-theme-textSec text-sm">Vue d'ensemble sur l'activité et l'état de facturation</p>
        </div>
        <button 
          onClick={fetchStats}
          disabled={refreshing}
          className="flex items-center gap-2 bg-theme-bg border border-theme-border text-theme-text px-4 py-2 rounded-lg hover:bg-theme-card transition-colors font-medium shadow-sm disabled:opacity-50"
        >
          <RefreshCw size={18} className={refreshing ? 'animate-spin' : ''} /> 
          Rafraîchir
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
        <div className="bg-theme-card rounded-xl border border-theme-border p-6 flex flex-col justify-between hover:shadow-md transition-shadow relative overflow-hidden">
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-theme-accent/10 dark:bg-theme-accent/20 rounded-full blur-xl pointer-events-none"></div>
          <div className="flex justify-between items-start mb-4 relative">
             <div className="bg-orange-100 text-orange-600 dark:bg-orange-900/50 dark:text-orange-400 p-3 rounded-xl border border-orange-200 dark:border-orange-800">
                <FileCheck2 size={24} />
             </div>
          </div>
          <div>
            <h2 className="text-4xl font-black text-theme-text mb-2">{stats.phasesTermineesNonFacturees}</h2>
            <p className="text-sm font-medium text-theme-textSec">Phases Terminées & Non Facturées</p>
          </div>
        </div>

        <div className="bg-theme-card rounded-xl border border-theme-border p-6 flex flex-col justify-between hover:shadow-md transition-shadow relative overflow-hidden">
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-red-500/10 dark:bg-red-500/20 rounded-full blur-xl pointer-events-none"></div>
          <div className="flex justify-between items-start mb-4 relative">
             <div className="bg-red-100 text-red-600 dark:bg-red-900/50 dark:text-red-400 p-3 rounded-xl border border-red-200 dark:border-red-800">
                <AlertCircle size={24} />
             </div>
          </div>
          <div>
            <h2 className="text-4xl font-black text-theme-text mb-2">{stats.phasesFactureesNonPayees}</h2>
            <p className="text-sm font-medium text-theme-textSec">Phases Facturées & Non Payées</p>
          </div>
        </div>

        <div className="bg-theme-card rounded-xl border border-theme-border p-6 flex flex-col justify-between hover:shadow-md transition-shadow relative overflow-hidden">
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-green-500/10 dark:bg-green-500/20 rounded-full blur-xl pointer-events-none"></div>
          <div className="flex justify-between items-start mb-4 relative">
             <div className="bg-green-100 text-green-600 dark:bg-green-900/50 dark:text-green-400 p-3 rounded-xl border border-green-200 dark:border-green-800">
                <BarChart2 size={24} />
             </div>
          </div>
          <div>
            <h2 className="text-4xl font-black text-theme-text mb-2">{stats.projetsEnCours}</h2>
            <p className="text-sm font-medium text-theme-textSec">Projets en Cours</p>
          </div>
        </div>

        <div className="bg-theme-card rounded-xl border border-theme-border p-6 flex flex-col justify-between hover:shadow-md transition-shadow relative overflow-hidden">
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-purple-500/10 dark:bg-purple-500/20 rounded-full blur-xl pointer-events-none"></div>
          <div className="flex justify-between items-start mb-4 relative">
             <div className="bg-purple-100 text-purple-600 dark:bg-purple-900/50 dark:text-purple-400 p-3 rounded-xl border border-purple-200 dark:border-purple-800">
                <Briefcase size={24} />
             </div>
          </div>
          <div>
            <h2 className="text-4xl font-black text-theme-text mb-2">{stats.projetsClotures}</h2>
            <p className="text-sm font-medium text-theme-textSec">Projets Clôturés</p>
          </div>
        </div>
      </div>
    </div>
  );
}