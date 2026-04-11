import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuthStore } from '../store/useAuthStore';
import { Plus, X, Loader2, CheckCircle, PlayCircle, Trash2 } from 'lucide-react';

export default function Projets() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const role = user.role?.code || user.role;
  console.log("[Projets] user brut localStorage =", localStorage.getItem('user'));
  console.log("[Projets] role extrait =", role);

  const [projets, setProjets] = useState([]);
  const [organismes, setOrganismes] = useState([]);
  const [employes, setEmployes] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    nom: '', code: '', dateDebut: '', dateFin: '', montant: '', organisme_id: '', chef_projet_id: ''
  });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);
  const [statusLoading, setStatusLoading] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [projRes, orgRes, empRes] = await Promise.all([
        api.get('/projets'),
        api.get('/organismes'),
        api.get('/employes')
      ]);
      setProjets(projRes.data);
      setOrganismes(orgRes.data);
      setEmployes(empRes.data);
    } catch (error) {
      console.error('Erreur chargement:', error);
    } finally {
      setPageLoading(false);
    }
  };

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true); setMessage('');
    try {
      await api.post('/projets', { ...formData, statut: 'EN_ATTENTE' });
      setMessage('Projet créé avec succès !');
      setFormData({ nom: '', code: '', dateDebut: '', dateFin: '', montant: '', organisme_id: '', chef_projet_id: '' });
      setIsModalOpen(false);
      fetchData();
    } catch (error) {
      setMessage('Erreur: ' + (error.response?.data?.message || error.message));
    } finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer ce projet ?')) return;
    try {
      await api.delete(`/projets/${id}`);
      fetchData();
    } catch (err) { alert('Erreur lors de la suppression'); }
  };

  const STATUT_LABELS = { VALIDE: 'validé ✓', EN_COURS: 'démarré ▶', TERMINE: 'terminé ✔' };

  const updateStatus = async (projet, newStatut) => {
    setStatusLoading(projet.id);
    try {
      const endpointMap = { VALIDE: 'valider', EN_COURS: 'demarrer', TERMINE: 'terminer' };
      const endpoint = endpointMap[newStatut];
      if (!endpoint) return;

      await api.put(`/projets/${projet.id}/${endpoint}`);
      await fetchData();

      setMessage(`Projet ${STATUT_LABELS[newStatut] || newStatut}`);
      setTimeout(() => setMessage(''), 3000);
    } catch (err) {
      console.error('[Projets] updateStatus erreur:', err.response?.data || err.message);
      setMessage('Erreur : ' + (err.response?.data?.error || err.response?.data?.message || err.message));
      setTimeout(() => setMessage(''), 5000);
    } finally {
      setStatusLoading(null);
    }
  };

  const getStatusBadge = (statut) => {
    switch (statut) {
      case 'TERMINE': return <span className="px-2 py-1 bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200 text-xs rounded-full font-bold">Terminé</span>;
      case 'EN_COURS': return <span className="px-2 py-1 bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300 text-xs rounded-full font-bold">En cours</span>;
      case 'VALIDE': return <span className="px-2 py-1 bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-300 text-xs rounded-full font-bold">Validé</span>;
      default: return <span className="px-2 py-1 bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200 text-xs rounded-full font-bold">En attente</span>;
    }
  };

  if (pageLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-theme-accent" size={32} /></div>;

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-theme-text mb-1">Gestion des Projets</h1>
          <p className="text-theme-textSec text-sm">Liste et suivi de tous les projets</p>
        </div>
        {(role === 'SECRETAIRE') && (
          <button
            onClick={() => setIsModalOpen(true)}
            className="flex items-center gap-2 bg-theme-accent text-white px-4 py-2 rounded-lg hover:opacity-90 transition-opacity font-medium"
          >
            <Plus size={20} /> Nouveau Projet
          </button>
        )}
      </div>

      {message && <div className="p-4 bg-green-100 text-green-800 rounded-lg">{message}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
        {projets.map(p => (
          <div key={p.id} className="bg-theme-card rounded-xl border border-theme-border p-5 flex flex-col justify-between transition-colors duration-200">
            <div>
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h3 className="text-lg font-bold text-theme-text">{p.nom}</h3>
                  <p className="text-sm text-theme-textSec">Code: {p.code}</p>
                </div>
                {getStatusBadge(p.statut)}
              </div>
              <p className="text-2xl font-bold text-theme-text mb-4">{p.montant?.toLocaleString()} <span className="text-sm font-normal text-theme-textSec">MAD</span></p>

              <div className="mb-4">
                <div className="flex justify-between text-xs text-theme-textSec mb-1">
                  <span>Avancement</span>
                  <span>{p.tauxAvancement || 0}%</span>
                </div>
                <div className="bg-theme-bg rounded-full h-2 overflow-hidden border border-theme-border">
                  <div className="bg-theme-accent h-full rounded-full transition-all" style={{ width: `${p.tauxAvancement || 0}%` }} />
                </div>
              </div>
            </div>

            <div className="flex gap-2 mt-4 pt-4 border-t border-theme-border justify-between items-center">
              <div className="flex gap-2">
                {/* DIRECTEUR - Valider (affiche pour EN_ATTENTE et aussi pour les projets déjà VALIDE si besoin) */}
                {role === 'DIRECTEUR' && p.statut !== 'EN_COURS' && p.statut !== 'TERMINE' && (
                  <button
                    onClick={() => updateStatus(p, 'VALIDE')}
                    disabled={statusLoading === p.id}
                    className="flex items-center gap-1 text-xs px-3 py-1.5 bg-purple-100 text-purple-700 dark:bg-purple-900 dark:text-purple-300 rounded-lg font-medium hover:bg-purple-200 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    {statusLoading === p.id
                      ? <Loader2 size={14} className="animate-spin" />
                      : <CheckCircle size={14} />}
                    Valider
                  </button>
                )}

                {/* CHEF_PROJET - Démarrer */}
                {role === 'CHEF_PROJET' && p.statut === 'VALIDE' && (
                  <button
                    onClick={() => updateStatus(p, 'EN_COURS')}
                    disabled={statusLoading === p.id}
                    className="flex items-center gap-1 text-xs px-3 py-1.5 bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300 rounded-lg font-medium hover:bg-blue-200 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    {statusLoading === p.id
                      ? <Loader2 size={14} className="animate-spin" />
                      : <PlayCircle size={14} />}
                    Démarrer
                  </button>
                )}
              </div>
              {(role === 'ADMIN') && (
                <button onClick={() => handleDelete(p.id)} className="text-red-500 hover:text-red-700 p-1.5 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/30 transition-colors">
                  <Trash2 size={18} />
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-theme-card rounded-2xl w-full max-w-lg shadow-2xl border border-theme-border overflow-hidden flex flex-col max-h-[90vh]">
            <div className="flex justify-between items-center p-6 border-b border-theme-border shrink-0">
              <h2 className="text-xl font-bold text-theme-text">Nouveau Projet</h2>
              <button onClick={() => setIsModalOpen(false)} className="text-theme-textSec hover:text-theme-text cursor-pointer p-1 rounded-lg hover:bg-theme-bg transition-colors">
                <X size={24} />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4 overflow-y-auto">
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Nom du projet</label>
                <input name="nom" value={formData.nom} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Code</label>
                <input name="code" value={formData.code} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-medium text-theme-text">Date de début</label>
                  <input name="dateDebut" type="date" value={formData.dateDebut} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-medium text-theme-text">Date de fin</label>
                  <input name="dateFin" type="date" value={formData.dateFin} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
                </div>
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Montant (MAD)</label>
                <input name="montant" type="number" value={formData.montant} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Organisme Client</label>
                <select name="organisme_id" value={formData.organisme_id} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]">
                  <option value="">Sélectionner un organisme</option>
                  {organismes.map(o => <option key={o.id} value={o.id}>{o.nom}</option>)}
                </select>
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Chef de projet</label>
                <select name="chef_projet_id" value={formData.chef_projet_id} onChange={handleChange} className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]">
                  <option value="">Sélectionner</option>
                  {employes.map(e => <option key={e.id} value={e.id}>{e.nom} {e.prenom}</option>)}
                </select>
              </div>

              <div className="mt-4 flex gap-3 pb-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 px-4 py-2.5 bg-theme-bg text-theme-text rounded-lg font-medium hover:opacity-80 transition-opacity border border-theme-border">
                  Annuler
                </button>
                <button type="submit" disabled={loading} className="flex-1 px-4 py-2.5 bg-theme-accent text-white rounded-lg font-medium hover:opacity-90 transition-opacity flex justify-center items-center">
                  {loading ? <Loader2 size={20} className="animate-spin" /> : 'Enregistrer Projet'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}