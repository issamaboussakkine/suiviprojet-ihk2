import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuthStore } from '../store/useAuthStore';
import { Plus, X, Loader2, FileCheck2, Trash2, Tag, Calendar, Banknote } from 'lucide-react';

export default function Factures() {
  const { user } = useAuthStore();
  const role = user?.role;
  const [factures, setFactures] = useState([]);
  const [phases, setPhases] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    phaseId: '', montant: '', dateEmission: ''
  });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);

  useEffect(() => {
    fetchFactures();
    fetchPhases();
  }, []);

  const fetchFactures = async () => {
    try {
      const response = await api.get('/factures');
      setFactures(response.data);
    } catch (error) {
      console.error('Erreur chargement factures:', error);
    } finally {
      setPageLoading(false);
    }
  };

  const fetchPhases = async () => {
    try {
      const response = await api.get('/phases');
      const phasesDisponibles = response.data.filter(p => p.etatRealisation && !p.etatFacturation);
      setPhases(phasesDisponibles);
    } catch (error) { console.error('Erreur phases:', error); }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => {
        const nextState = { ...prev, [name]: value };
        if (name === 'phaseId') {
            const phase = phases.find(p => p.id == value);
            if (phase && phase.montant) nextState.montant = phase.montant;
        }
        return nextState;
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); setLoading(true); setMessage('');
    try {
      await api.post('/factures', {
        phaseId: parseInt(formData.phaseId),
        montant: parseFloat(formData.montant),
        dateEmission: formData.dateEmission
      });
      setMessage('Facture créée avec succès !');
      setFormData({ phaseId: '', montant: '', dateEmission: '' });
      setIsModalOpen(false);
      fetchFactures();
      fetchPhases();
    } catch (error) {
      setMessage('Erreur: ' + (error.response?.data?.message || error.message));
    } finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer cette facture ?')) return;
    try {
      await api.delete(`/factures/${id}`);
      fetchFactures();
    } catch (error) {
      setMessage('Erreur lors de la suppression');
    }
  };

  const getPhaseDisplay = (phase) => {
    return phase.description || phase.libelle || phase.nom || `Phase ${phase.id}`;
  };

  if (pageLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-theme-accent" size={32} /></div>;

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-theme-text mb-1">Gestion des Factures</h1>
          <p className="text-theme-textSec text-sm">Suivi de la facturation et des encaissements</p>
        </div>
        {(role === 'SECRETAIRE' || role === 'ADMIN') && (
          <button 
            onClick={() => setIsModalOpen(true)}
            className="flex items-center gap-2 bg-theme-accent text-white px-4 py-2 rounded-lg hover:opacity-90 transition-opacity font-medium"
          >
            <Plus size={20} /> Nouvelle Facture
          </button>
        )}
      </div>

      {message && <div className="p-4 bg-theme-card border border-theme-border text-theme-text font-medium rounded-lg">{message}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {factures.map((f) => {
          let statutColor = f.etatPaiement ? 'text-green-700 bg-green-100 dark:bg-green-900 dark:text-green-300' : (f.etatFacturation ? 'text-yellow-700 bg-yellow-100 dark:bg-yellow-900 dark:text-yellow-300' : 'text-red-700 bg-red-100 dark:bg-red-900 dark:text-red-300');
          let statutLabel = f.etatPaiement ? 'Payée' : (f.etatFacturation ? 'Facturée' : 'Non facturée');
          
          return (
          <div key={f.id} className="bg-theme-card rounded-xl border border-theme-border p-5 flex flex-col justify-between transition-colors duration-200">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-xl bg-theme-bg border border-theme-border flex items-center justify-center shrink-0">
                    <FileCheck2 size={24} className="text-theme-accent" />
                </div>
                <div>
                  <h3 className="text-lg font-bold text-theme-text leading-tight">Facture #{f.id}</h3>
                  <p className="text-xs font-semibold mt-1 uppercase tracking-wider"><span className={`px-2 py-0.5 rounded-md ${statutColor}`}>{statutLabel}</span></p>
                </div>
              </div>
            </div>

            <div className="space-y-3 mb-4 flex-1">
                <div className="flex items-center gap-2 text-sm text-theme-text font-bold">
                    <Banknote size={16} className="text-theme-textSec shrink-0" />
                    <span className="text-theme-text">{f.montant?.toLocaleString()} MAD</span>
                </div>
                
                <div className="flex items-start gap-2 pt-2 mt-2 border-t border-theme-border/50 text-sm text-theme-text">
                    <Tag size={16} className="text-theme-textSec shrink-0 mt-0.5" />
                    <span className="font-medium text-theme-textSec">Rattachée: Phase ID {f.phaseId}</span>
                </div>

                <div className="flex items-center gap-2 text-sm text-theme-textSec font-medium">
                    <Calendar size={16} className="text-theme-textSec shrink-0" />
                    <span>Emise le: {f.dateEmission}</span>
                </div>
            </div>

            {(role === 'SECRETAIRE' || role === 'ADMIN') && (
                <div className="pt-4 border-t border-theme-border text-right mt-auto">
                    <button onClick={() => handleDelete(f.id)} className="text-red-500 hover:text-red-700 p-1.5 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/30 transition-colors">
                        <Trash2 size={18} />
                    </button>
                </div>
            )}
          </div>
        )})}
        {factures.length === 0 && (
            <div className="col-span-full p-8 text-center text-theme-textSec">Aucune facture générée.</div>
        )}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-theme-card rounded-2xl w-full max-w-md shadow-2xl border border-theme-border overflow-hidden flex flex-col max-h-[90vh]">
            <div className="flex justify-between items-center p-6 border-b border-theme-border shrink-0">
              <h2 className="text-xl font-bold text-theme-text">Générer Facture</h2>
              <button onClick={() => setIsModalOpen(false)} className="text-theme-textSec hover:text-theme-text cursor-pointer p-1 rounded-lg hover:bg-theme-bg transition-colors">
                <X size={24} />
              </button>
            </div>
            
            <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4 overflow-y-auto">
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Phase à facturer</label>
                <select name="phaseId" value={formData.phaseId} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]">
                  <option value="">-- Sélectionner une phase terminée non facturée --</option>
                  {phases.map(phase => <option key={phase.id} value={phase.id}>{getPhaseDisplay(phase)} - {phase.montant?.toLocaleString()} MAD</option>)}
                </select>
                {phases.length === 0 && <p className="text-xs text-orange-500">Aucune phase disponible pour facturation.</p>}
              </div>

              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Montant (MAD)</label>
                <input name="montant" type="number" value={formData.montant} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>

              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Date d'émission</label>
                <input name="dateEmission" type="date" value={formData.dateEmission} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>

              <div className="mt-4 flex gap-3 pb-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 px-4 py-2.5 bg-theme-bg text-theme-text rounded-lg font-medium hover:opacity-80 transition-opacity border border-theme-border">
                  Annuler
                </button>
                <button type="submit" disabled={loading} className="flex-1 px-4 py-2.5 bg-theme-accent text-white rounded-lg font-medium hover:opacity-90 transition-opacity flex justify-center items-center">
                  {loading ? <Loader2 size={20} className="animate-spin" /> : 'Créer Facture'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}