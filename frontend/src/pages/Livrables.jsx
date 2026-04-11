import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuthStore } from '../store/useAuthStore';
import { Plus, X, Loader2, FileText, Trash2, Download, Upload, Tag } from 'lucide-react';

export default function Livrables() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const role = user.role?.code || user.role;
  console.log("role =", role);

  const [livrables, setLivrables] = useState([]);
  const [phases, setPhases] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    phaseId: '', code: '', libelle: '', description: '', fichier: null
  });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);

  useEffect(() => {
    fetchLivrables();
    fetchPhases();
  }, []);

  const fetchLivrables = async () => {
    try {
      const response = await api.get('/livrables');
      setLivrables(response.data);
    } catch (error) {
      console.error('Erreur chargement livrables:', error);
    } finally {
      setPageLoading(false);
    }
  };

  const fetchPhases = async () => {
    try {
      const response = await api.get('/phases');
      setPhases(response.data);
    } catch (error) { console.error('Erreur phases:', error); }
  };

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === 'fichier') {
      setFormData({ ...formData, fichier: files[0] });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); setLoading(true); setMessage('');
    try {
      const data = new FormData();
      data.append('phaseId', formData.phaseId);
      data.append('code', formData.code);
      data.append('libelle', formData.libelle);
      data.append('description', formData.description);
      if (formData.fichier) {
        data.append('fichier', formData.fichier);
      }

      await api.post('/livrables/upload', data, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      setMessage('Livrable déposé avec succès !');
      setFormData({ phaseId: '', code: '', libelle: '', description: '', fichier: null });
      setIsModalOpen(false);
      fetchLivrables();
    } catch (error) {
      setMessage('Erreur: ' + (error.response?.data?.message || 'Erreur lors du dépôt'));
    } finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer ce livrable ? Cette action est irréversible.')) return;
    try {
      await api.delete(`/livrables/${id}`);
      fetchLivrables();
    } catch (error) {
      setMessage('Erreur lors de la suppression');
    }
  };

  const getPhaseDisplay = (phase) => {
    const nom = phase.nom || phase.libelle || `Phase ${phase.id}`;
    const desc = phase.description ? ` — ${phase.description.slice(0, 50)}${phase.description.length > 50 ? '…' : ''}` : '';
    return `${nom}${desc}`;
  };

  if (pageLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-theme-accent" size={32} /></div>;

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-theme-text mb-1">Dépôt des Livrables</h1>
          <p className="text-theme-textSec text-sm">Gestion des documents et résultats de chaque phase</p>
        </div>
        {(role === 'COLLABORATEUR' || role === 'CHEF_PROJET') && (
          <button
            onClick={() => setIsModalOpen(true)}
            className="flex items-center gap-2 bg-theme-accent text-white px-4 py-2 rounded-lg hover:opacity-90 transition-opacity font-medium"
          >
            <Upload size={20} /> Déposer Livrable
          </button>
        )}
      </div>

      {message && <div className="p-4 bg-theme-card border border-theme-border text-theme-text font-medium rounded-lg">{message}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {livrables.map((l) => (
          <div key={l.id} className="bg-theme-card rounded-xl border border-theme-border p-5 flex flex-col justify-between transition-colors duration-200">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-xl bg-theme-bg border border-theme-border flex items-center justify-center shrink-0">
                  <FileText size={24} className="text-theme-accent" />
                </div>
                <div>
                  <h3 className="text-lg font-bold text-theme-text leading-tight">{l.libelle || l.code}</h3>
                  <p className="text-xs font-semibold text-theme-textSec mt-1 uppercase tracking-wider">{l.code}</p>
                </div>
              </div>
            </div>

            <div className="space-y-3 mb-4 flex-1">
              <p className="text-sm text-theme-text line-clamp-3">{l.description}</p>

              <div className="flex items-start gap-2 pt-2 mt-2 border-t border-theme-border/50 text-sm text-theme-text">
                <Tag size={16} className="text-theme-textSec shrink-0 mt-0.5" />
                <span className="font-medium text-theme-textSec">Phase rattachée: {l.phaseId}</span>
              </div>
            </div>

            <div className="flex gap-2 mt-4 pt-4 border-t border-theme-border justify-between items-center">
              <div className="flex gap-2">
                {l.chemin && (
                  <a
                    href={`http://localhost:8081/api/livrables/download/${l.id}`}
                    target="_blank" rel="noopener noreferrer"
                    className="flex items-center gap-1.5 text-xs px-3 py-1.5 bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300 rounded-lg font-medium hover:bg-blue-200 transition-colors"
                  >
                    <Download size={14} /> Télécharger
                  </a>
                )}
              </div>
              {(role === 'ADMIN' || role === 'CHEF_PROJET') && (
                <button onClick={() => handleDelete(l.id)} className="text-red-500 hover:text-red-700 p-1.5 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/30 transition-colors">
                  <Trash2 size={18} />
                </button>
              )}
            </div>
          </div>
        ))}
        {livrables.length === 0 && (
          <div className="col-span-full p-8 text-center text-theme-textSec">Aucun livrable déposé.</div>
        )}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-theme-card rounded-2xl w-full max-w-md shadow-2xl border border-theme-border overflow-hidden flex flex-col max-h-[90vh]">
            <div className="flex justify-between items-center p-6 border-b border-theme-border shrink-0">
              <h2 className="text-xl font-bold text-theme-text">Nouveau Livrable</h2>
              <button onClick={() => setIsModalOpen(false)} className="text-theme-textSec hover:text-theme-text cursor-pointer p-1 rounded-lg hover:bg-theme-bg transition-colors">
                <X size={24} />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4 overflow-y-auto">
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Phase du projet</label>
                <select name="phaseId" value={formData.phaseId} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]">
                  <option value="">-- Sélectionner une phase --</option>
                  {phases.map(phase => <option key={phase.id} value={phase.id}>{getPhaseDisplay(phase)}</option>)}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-medium text-theme-text">Code Livrable</label>
                  <input name="code" value={formData.code} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-medium text-theme-text">Titre / Libellé</label>
                  <input name="libelle" value={formData.libelle} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
                </div>
              </div>

              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Description</label>
                <textarea name="description" value={formData.description} onChange={handleChange} className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark] min-h-[80px]" />
              </div>

              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Fichier</label>
                <input type="file" name="fichier" onChange={handleChange} className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-sm file:font-semibold file:bg-theme-accent file:text-white hover:file:opacity-90" />
              </div>

              <div className="mt-4 flex gap-3 pb-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 px-4 py-2.5 bg-theme-bg text-theme-text rounded-lg font-medium hover:opacity-80 transition-opacity border border-theme-border">
                  Annuler
                </button>
                <button type="submit" disabled={loading} className="flex-1 px-4 py-2.5 bg-theme-accent text-white rounded-lg font-medium hover:opacity-90 transition-opacity flex justify-center items-center">
                  {loading ? <Loader2 size={20} className="animate-spin" /> : 'Créer et Uploader'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}