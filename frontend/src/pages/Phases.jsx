import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuthStore } from '../store/useAuthStore';
import { Plus, X, Loader2, PlayCircle, CheckCircle, Trash2, Upload } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function Phases() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const role = user.role?.code || user.role;
  const userId = user.id;
  console.log("role =", role);
  console.log("userId =", userId);

  const navigate = useNavigate();

  const [phases, setPhases] = useState([]);
  const [projets, setProjets] = useState([]);
  const [affectations, setAffectations] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    nom: '', description: '', dateDebut: '', dateFin: '', montant: '', projet_id: ''
  });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [phRes, prjRes, affRes] = await Promise.all([
        api.get('/phases'),
        api.get('/projets'),
        api.get('/affectations')
      ]);
      setPhases(phRes.data);
      setProjets(prjRes.data);
      setAffectations(affRes.data);
    } catch (error) {
      console.error(error);
    } finally {
      setPageLoading(false);
    }
  };

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault(); setLoading(true); setMessage('');
    try {
      await api.post('/phases', {
        libelle: formData.nom,
        description: formData.description,
        dateDebut: formData.dateDebut,
        dateFin: formData.dateFin,
        montant: formData.montant,
        projet_id: formData.projet_id
      });
      setMessage('Phase ajoutée avec succès !');
      setFormData({ nom: '', description: '', dateDebut: '', dateFin: '', montant: '', projet_id: '' });
      setIsModalOpen(false);
      fetchData();
    } catch (error) {
      setMessage('Erreur: ' + (error.response?.data?.message || 'Vérifiez les données saisies.'));
    } finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer cette phase ?')) return;
    try {
      await api.delete(`/phases/${id}`);
      fetchData();
    } catch (err) { alert('Erreur lors de la suppression'); }
  };

  // Mise à jour du statut via l'API
  const updateStatus = async (phase, isTerminee) => {
    try {
      const response = await api.put(`/phases/${phase.id}/update-status`, {
        etatRealisation: isTerminee,
        statut: isTerminee ? 'TERMINEE' : 'EN_COURS'
      });
      console.log("Réponse:", response.data);
      window.location.reload(); // Recharge la page pour afficher le changement
    } catch (err) {
      console.error("Erreur:", err);
      alert('Erreur mise à jour statut');
    }
  };

  // Vérifier si la phase est affectée à l'utilisateur connecté
  const isPhaseAffectee = (phaseId) => {
    return affectations.some(aff => aff.phaseId === phaseId && aff.employeId === userId);
  };

  // Filtrer les phases pour le collaborateur
  const phasesFiltrees = role === 'COLLABORATEUR'
    ? phases.filter(p => affectations.some(aff => aff.phaseId === p.id && aff.employeId === userId))
    : phases;

  const getStatusBadge = (phase) => {
    const isTerminee = phase.etatRealisation === true || phase.etatRealisation === 1;
    if (isTerminee) {
      return <span className="px-2 py-1 bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200 text-xs rounded-full font-bold">Terminée</span>;
    }
    // Vérifie si la phase a commencé (date début <= aujourd'hui)
    const today = new Date().toISOString().split('T')[0];
    if (phase.dateDebut && phase.dateDebut <= today) {
      return <span className="px-2 py-1 bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200 text-xs rounded-full font-bold">En cours</span>;
    }
    return <span className="px-2 py-1 bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200 text-xs rounded-full font-bold">Non commencée</span>;
  };

  if (pageLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-theme-accent" size={32} /></div>;

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-theme-text mb-1">Gestion des Phases</h1>
          <p className="text-theme-textSec text-sm">Suivi des étapes de chaque projet</p>
        </div>
        {(role === 'CHEF_PROJET') && (
          <button
            onClick={() => setIsModalOpen(true)}
            className="flex items-center gap-2 bg-theme-accent text-white px-4 py-2 rounded-lg hover:opacity-90 transition-opacity font-medium"
          >
            <Plus size={20} /> Nouvelle Phase
          </button>
        )}
      </div>

      {message && <div className="p-4 bg-green-100 text-green-800 rounded-lg">{message}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
        {phasesFiltrees.map(p => {
          const currentStatut = p.etatRealisation ? 'TERMINEE' : (p.dateDebut && p.dateDebut <= new Date().toISOString().split('T')[0]) ? 'EN_COURS' : 'NON_COMMENCEE';
          const estAffectee = isPhaseAffectee(p.id);
          const isTerminee = p.etatRealisation === true || p.etatRealisation === 1;

          return (
            <div key={p.id} className="bg-theme-card rounded-xl border border-theme-border p-5 flex flex-col justify-between transition-colors duration-200">
              <div>
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="text-lg font-bold text-theme-text">{p.libelle || p.nom}</h3>
                    <p className="text-xs text-theme-textSec mt-1 line-clamp-2">{p.description}</p>
                  </div>
                  {getStatusBadge(p)}
                </div>

                <div className="bg-theme-bg p-3 rounded-lg border border-theme-border mb-4">
                  <p className="text-xs text-theme-textSec mb-1">Projet rattaché</p>
                  <p className="text-sm font-medium text-theme-text">{p.projet?.nom || `ID: ${p.projet_id}`}</p>
                </div>

                <div className="flex justify-between items-center mb-4">
                  <div>
                    <p className="text-xs text-theme-textSec">Budget Phase</p>
                    <p className="font-bold text-theme-text">{p.montant?.toLocaleString()} MAD</p>
                  </div>
                </div>
              </div>

              <div className="flex gap-2 mt-4 pt-4 border-t border-theme-border justify-between items-center">
                <div className="flex gap-2 flex-wrap">
                  {/* Bouton Démarrer */}
                  {role === 'COLLABORATEUR' && estAffectee && !isTerminee && currentStatut === 'NON_COMMENCEE' && (
                    <button onClick={() => updateStatus(p, false)} className="flex items-center gap-1 text-xs px-3 py-1.5 bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300 rounded-lg font-medium hover:bg-blue-200 transition-colors">
                      <PlayCircle size={14} /> Démarrer
                    </button>
                  )}

                  {/* Boutons pour phase en cours */}
                  {role === 'COLLABORATEUR' && estAffectee && !isTerminee && currentStatut === 'EN_COURS' && (
                    <>
                      <button onClick={() => updateStatus(p, true)} className="flex items-center gap-1 text-xs px-3 py-1.5 bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300 rounded-lg font-medium hover:bg-green-200 transition-colors">
                        <CheckCircle size={14} /> Terminer
                      </button>
                      <button onClick={() => navigate('/livrables')} className="flex items-center gap-1 text-xs px-3 py-1.5 bg-purple-100 text-purple-700 dark:bg-purple-900 dark:text-purple-300 rounded-lg font-medium hover:bg-purple-200 transition-colors">
                        <Upload size={14} /> Livrable
                      </button>
                    </>
                  )}
                </div>
                {(role === 'ADMIN' || role === 'CHEF_PROJET') && (
                  <button onClick={() => handleDelete(p.id)} className="text-red-500 hover:text-red-700 p-1.5 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/30 transition-colors">
                    <Trash2 size={18} />
                  </button>
                )}
              </div>
            </div>
          )
        })}
        {phasesFiltrees.length === 0 && role === 'COLLABORATEUR' && (
          <div className="col-span-full p-8 text-center text-theme-textSec">Aucune phase ne vous est affectée.</div>
        )}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-theme-card rounded-2xl w-full max-w-lg shadow-2xl border border-theme-border overflow-hidden flex flex-col max-h-[90vh]">
            <div className="flex justify-between items-center p-6 border-b border-theme-border shrink-0">
              <h2 className="text-xl font-bold text-theme-text">Nouvelle Phase</h2>
              <button onClick={() => setIsModalOpen(false)} className="text-theme-textSec hover:text-theme-text cursor-pointer p-1 rounded-lg hover:bg-theme-bg transition-colors">
                <X size={24} />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4 overflow-y-auto">
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Projet</label>
                <select name="projet_id" value={formData.projet_id} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent">
                  <option value="">-- Sélectionner un projet --</option>
                  {projets.map(p => <option key={p.id} value={p.id}>{p.nom}</option>)}
                </select>
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Nom de la phase</label>
                <input name="nom" value={formData.nom} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent" />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Description</label>
                <textarea name="description" value={formData.description} onChange={handleChange} className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent min-h-[80px]" />
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
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Budget (MAD)</label>
                <input name="montant" type="number" value={formData.montant} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent" />
              </div>

              <div className="mt-4 flex gap-3 pb-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 px-4 py-2.5 bg-theme-bg text-theme-text rounded-lg font-medium hover:opacity-80 transition-opacity border border-theme-border">
                  Annuler
                </button>
                <button type="submit" disabled={loading} className="flex-1 px-4 py-2.5 bg-theme-accent text-white rounded-lg font-medium hover:opacity-90 transition-opacity flex justify-center items-center">
                  {loading ? <Loader2 size={20} className="animate-spin" /> : 'Enregistrer'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}