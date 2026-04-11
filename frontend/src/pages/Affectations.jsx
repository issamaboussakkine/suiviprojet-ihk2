import { useState, useEffect } from 'react';
import api from '../services/api';
import { Plus, X, Loader2, Link as LinkIcon, Trash2, Calendar, User, Tag, Building2 } from 'lucide-react';

export default function Affectations() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const role = user.role?.code || user.role;
  console.log("role =", role);

  const [affectations, setAffectations] = useState([]);
  const [employes, setEmployes] = useState([]);
  const [phases, setPhases] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    employeId: '', phaseId: '', dateDebut: '', dateFin: ''
  });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);

  useEffect(() => {
    fetchAffectations();
    fetchEmployes();
    fetchPhases();
  }, []);

  const fetchAffectations = async () => {
    try {
      const response = await api.get('/affectations');
      console.log("Affectations reçues:", response.data);
      setAffectations(response.data);
    } catch (error) {
      console.error('Erreur chargement affectations:', error);
    } finally {
      setPageLoading(false);
    }
  };

  const fetchEmployes = async () => {
    try {
      const response = await api.get('/employes');
      setEmployes(response.data);
    } catch (error) { console.error('Erreur employés:', error); }
  };

  const fetchPhases = async () => {
    try {
      const response = await api.get('/phases');
      setPhases(response.data);
    } catch (error) { console.error('Erreur phases:', error); }
  };

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault(); setLoading(true); setMessage('');
    try {
      await api.post('/affectations', formData);
      setMessage('Affectation créée avec succès !');
      setFormData({ employeId: '', phaseId: '', dateDebut: '', dateFin: '' });
      setIsModalOpen(false);
      fetchAffectations();
    } catch (error) {
      setMessage('Erreur: ' + (error.response?.data?.message || 'Vérifiez la disponibilité de l\'employé.'));
    } finally { setLoading(false); }
  };

  const handleDelete = async (employeId, phaseId) => {
    if (!window.confirm('Supprimer cette affectation ?')) return;
    try {
      await api.delete(`/affectations/${employeId}/${phaseId}`);
      fetchAffectations();
    } catch (error) {
      setMessage('Erreur lors de la suppression');
    }
  };

  const getPhaseDisplay = (phase) => {
    const nom = phase.libelle || phase.nom || `Phase ${phase.id}`;
    const desc = phase.description ? ` — ${phase.description.slice(0, 50)}${phase.description.length > 50 ? '…' : ''}` : '';
    const montant = phase.montant ? ` (${phase.montant.toLocaleString()} MAD)` : '';
    return `${nom}${desc}${montant}`;
  };

  const getEmployeDisplay = (emp) => {
    if (emp.nom && emp.prenom) return `${emp.nom} ${emp.prenom}`;
    if (emp.nom) return emp.nom;
    if (emp.login) return emp.login;
    return `Employé ${emp.id}`;
  };

  if (pageLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-theme-accent" size={32} /></div>;

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-theme-text mb-1">Affectations</h1>
          <p className="text-theme-textSec text-sm">Organisez les assignations des ressources aux phases du projet</p>
        </div>
        {(role === 'CHEF_PROJET') && (
          <button
            onClick={() => setIsModalOpen(true)}
            className="flex items-center gap-2 bg-theme-accent text-white px-4 py-2 rounded-lg hover:opacity-90 transition-opacity font-medium"
          >
            <Plus size={20} /> Nouvelle Affectation
          </button>
        )}
      </div>

      {message && <div className="p-4 bg-theme-card border border-theme-border text-theme-text font-medium rounded-lg">{message}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {affectations.map((aff, index) => (
          <div key={`${aff.employeId}-${aff.phaseId}-${index}`} className="bg-theme-card rounded-xl border border-theme-border p-5 flex flex-col justify-between transition-colors duration-200">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-xl bg-theme-bg border border-theme-border flex items-center justify-center shrink-0">
                  <LinkIcon size={24} className="text-theme-accent" />
                </div>
                <div>
                  <h3 className="flex items-center gap-1.5 text-lg font-bold text-theme-text">
                    <User size={16} />{aff.employeNom || aff.employePrenom || `Employé ${aff.employeId}`}
                  </h3>
                </div>
              </div>
            </div>

            <div className="space-y-3 mb-4 flex-1">
              <div className="flex items-start gap-2 text-sm text-theme-text">
                <Tag size={16} className="text-theme-textSec shrink-0 mt-0.5" />
                <span className="line-clamp-2 font-medium">{aff.phaseLibelle || `Phase ${aff.phaseId}`}</span>
              </div>
              <div className="flex items-start gap-2 text-sm text-theme-text">
                <Building2 size={16} className="text-theme-textSec shrink-0 mt-0.5" />
                <span>{aff.projetNom || `Projet ${aff.projetId || 'Inconnu'}`}</span>
              </div>
              <div className="flex flex-col gap-1 mt-2 pt-2 border-t border-theme-border/50 text-sm font-medium text-theme-textSec">
                <div className="flex items-center gap-2"><Calendar size={14} /> Début: <span className="text-theme-text">{aff.dateDebut}</span></div>
                <div className="flex items-center gap-2"><Calendar size={14} /> Fin: <span className="text-theme-text">{aff.dateFin}</span></div>
              </div>
            </div>

            {(role === 'ADMIN' || role === 'CHEF_PROJET') && (
              <div className="pt-4 border-t border-theme-border text-right mt-auto">
                <button onClick={() => handleDelete(aff.employeId, aff.phaseId)} className="text-red-500 hover:text-red-700 p-1.5 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/30 transition-colors">
                  <Trash2 size={18} />
                </button>
              </div>
            )}
          </div>
        ))}
        {affectations.length === 0 && (
          <div className="col-span-full p-8 text-center text-theme-textSec">Aucune affectation trouvée.</div>
        )}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-theme-card rounded-2xl w-full max-w-md shadow-2xl border border-theme-border overflow-hidden flex flex-col max-h-[90vh]">
            <div className="flex justify-between items-center p-6 border-b border-theme-border shrink-0">
              <h2 className="text-xl font-bold text-theme-text">Créer Affectation</h2>
              <button onClick={() => setIsModalOpen(false)} className="text-theme-textSec hover:text-theme-text cursor-pointer p-1 rounded-lg hover:bg-theme-bg transition-colors">
                <X size={24} />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4 overflow-y-auto">
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Employé (Ressource)</label>
                <select name="employeId" value={formData.employeId} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent">
                  <option value="">-- Sélectionner un employé --</option>
                  {employes.map(emp => <option key={emp.id} value={emp.id}>{getEmployeDisplay(emp)}</option>)}
                </select>
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Phase du projet</label>
                <select name="phaseId" value={formData.phaseId} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent">
                  <option value="">-- Sélectionner une phase --</option>
                  {phases.map(phase => <option key={phase.id} value={phase.id}>{getPhaseDisplay(phase)}</option>)}
                </select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-medium text-theme-text">Date de début</label>
                  <input name="dateDebut" type="date" value={formData.dateDebut} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent" />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-medium text-theme-text">Date de fin</label>
                  <input name="dateFin" type="date" value={formData.dateFin} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent" />
                </div>
              </div>

              <div className="mt-4 flex gap-3 pb-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 px-4 py-2.5 bg-theme-bg text-theme-text rounded-lg font-medium hover:opacity-80 transition-opacity border border-theme-border">
                  Annuler
                </button>
                <button type="submit" disabled={loading} className="flex-1 px-4 py-2.5 bg-theme-accent text-white rounded-lg font-medium hover:opacity-90 transition-opacity flex justify-center items-center">
                  {loading ? <Loader2 size={20} className="animate-spin" /> : 'Affecter Ressource'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}