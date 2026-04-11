import { useState, useEffect } from 'react';
import api from '../services/api';
import { Plus, X, Loader2, Trash2, Receipt } from 'lucide-react';

export default function Factures() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const role = user.role?.code || user.role;
  const [factures, setFactures] = useState([]);
  const [phases, setPhases] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({ phaseId: '', dateFacture: '' });
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
      console.log("Factures reçues:", response.data);
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
      // Filtrer les phases terminées non facturées
      const phasesDisponibles = response.data.filter(p => p.etatRealisation && !p.etatFacturation);
      setPhases(phasesDisponibles);
    } catch (error) {
      console.error('Erreur chargement phases:', error);
    }
  };

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault(); setLoading(true); setMessage('');
    try {
      await api.post('/factures', { phaseId: formData.phaseId, dateFacture: formData.dateFacture });
      setMessage('Facture créée avec succès !');
      setFormData({ phaseId: '', dateFacture: '' });
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

  const getStatutLabel = (facture) => {
    // Vérifier si la facture est liée à une phase payée
    if (facture.phasePayee) return 'Payée';
    if (facture.phaseFacturee) return 'Facturée';
    return 'Non facturée';
  };

  if (pageLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-theme-accent" size={32} /></div>;

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-theme-text mb-1">Gestion des Factures</h1>
          <p className="text-theme-textSec text-sm">Suivi de la facturation et des encaissements</p>
        </div>
        {(role === 'COMPTABLE') && (
          <button
            onClick={() => setIsModalOpen(true)}
            className="flex items-center gap-2 bg-theme-accent text-white px-4 py-2 rounded-lg hover:opacity-90 transition-opacity font-medium"
          >
            <Plus size={20} /> Nouvelle Facture
          </button>
        )}
      </div>

      {message && <div className="p-4 bg-green-100 text-green-800 rounded-lg">{message}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {factures.map(f => (
          <div key={f.id} className="bg-theme-card rounded-xl border border-theme-border p-5 flex flex-col justify-between transition-colors duration-200">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-xl bg-theme-bg border border-theme-border flex items-center justify-center">
                  <Receipt size={24} className="text-theme-accent" />
                </div>
                <div>
                  <h3 className="text-lg font-bold text-theme-text">Facture #{f.id}</h3>
                  <p className="text-xs text-theme-textSec">{getStatutLabel(f)}</p>
                </div>
              </div>
            </div>

            <div className="space-y-2 mb-4">
              <p className="text-2xl font-bold text-theme-text">{f.montant?.toLocaleString()} MAD</p>
              <p className="text-sm text-theme-textSec">Phase: {f.phaseLibelle || `ID ${f.phaseId}`}</p>
              <p className="text-sm text-theme-textSec">Date: {f.dateFacture || 'Non définie'}</p>
            </div>

            {(role === 'COMPTABLE') && (
              <div className="pt-4 border-t border-theme-border text-right">
                <button onClick={() => handleDelete(f.id)} className="text-red-500 hover:text-red-700 p-1.5 rounded-lg hover:bg-red-50 transition-colors">
                  <Trash2 size={18} />
                </button>
              </div>
            )}
          </div>
        ))}
        {factures.length === 0 && (
          <div className="col-span-full p-8 text-center text-theme-textSec">Aucune facture trouvée.</div>
        )}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-theme-card rounded-2xl w-full max-w-md shadow-2xl border border-theme-border overflow-hidden">
            <div className="flex justify-between items-center p-6 border-b border-theme-border">
              <h2 className="text-xl font-bold text-theme-text">Créer une facture</h2>
              <button onClick={() => setIsModalOpen(false)} className="text-theme-textSec hover:text-theme-text">
                <X size={24} />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4">
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Phase</label>
                <select name="phaseId" value={formData.phaseId} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text">
                  <option value="">-- Sélectionner une phase terminée --</option>
                  {phases.map(p => (
                    <option key={p.id} value={p.id}>
                      {p.libelle || p.nom} - {p.montant?.toLocaleString()} MAD
                    </option>
                  ))}
                </select>
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Date d'émission</label>
                <input type="date" name="dateFacture" value={formData.dateFacture} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text" />
              </div>

              <div className="flex gap-3 mt-4">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 px-4 py-2 bg-theme-bg text-theme-text rounded-lg border border-theme-border">
                  Annuler
                </button>
                <button type="submit" disabled={loading} className="flex-1 px-4 py-2 bg-theme-accent text-white rounded-lg">
                  {loading ? <Loader2 size={20} className="animate-spin" /> : 'Créer'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}