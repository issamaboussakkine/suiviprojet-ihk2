import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuthStore } from '../store/useAuthStore';
import { Plus, X, Loader2, Trash2, Mail, User } from 'lucide-react';

export default function Employes() {
  const { user } = useAuthStore();
  const role = user?.role;
  const [employes, setEmployes] = useState([]);
  const [profils, setProfils] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    nom: '', prenom: '', email: '', login: '', password: '', profilId: ''
  });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);

  useEffect(() => {
    fetchEmployes();
    fetchProfils();
  }, []);

  const fetchEmployes = async () => {
    try {
      const response = await api.get('/employes');
      setEmployes(response.data);
    } catch (error) {
      console.error('Erreur chargement employés:', error);
    } finally {
      setPageLoading(false);
    }
  };

  const fetchProfils = async () => {
    try {
      const response = await api.get('/profils');
      setProfils(response.data);
    } catch (error) {
      console.error('Erreur chargement profils:', error);
    }
  };

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault(); setLoading(true); setMessage('');
    try {
      await api.post('/employes', formData);
      setMessage('Employé créé avec succès !');
      setFormData({ nom: '', prenom: '', email: '', login: '', password: '', profilId: '' });
      setIsModalOpen(false);
      fetchEmployes();
    } catch (error) {
      setMessage('Erreur: ' + (error.response?.data?.message || 'Identifiant déjà existant ?'));
    } finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer cet employé ?')) return;
    try {
      await api.delete(`/employes/${id}`);
      fetchEmployes();
    } catch (error) {
      setMessage('Erreur lors de la suppression');
    }
  };

  if (pageLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-theme-accent" size={32} /></div>;

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-theme-text mb-1">Administration Personnel</h1>
          <p className="text-theme-textSec text-sm">Gestion des collaborateurs et profils</p>
        </div>
        {(role === 'ADMIN') && (
          <button 
            onClick={() => setIsModalOpen(true)}
            className="flex items-center gap-2 bg-theme-accent text-white px-4 py-2 rounded-lg hover:opacity-90 transition-opacity font-medium"
          >
            <Plus size={20} /> Nouvel Employé
          </button>
        )}
      </div>

      {message && <div className="p-4 bg-theme-card border border-theme-border text-theme-text font-medium rounded-lg">{message}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {employes.map(e => (
          <div key={e.id} className="bg-theme-card rounded-xl border border-theme-border p-5 flex flex-col justify-between transition-colors duration-200">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-full bg-theme-bg border border-theme-border flex items-center justify-center">
                    <User size={24} className="text-theme-accent" />
                </div>
                <div>
                  <h3 className="text-lg font-bold text-theme-text">{e.nom} {e.prenom}</h3>
                  <p className="text-sm font-medium text-theme-textSec">{e.login}</p>
                </div>
              </div>
            </div>

            <div className="space-y-2 mb-4">
                <div className="flex items-center gap-2 text-sm text-theme-text">
                    <Mail size={16} className="text-theme-textSec" />
                    <span>{e.email || 'Aucun email'}</span>
                </div>
                <div className="flex flex-wrap gap-2 mt-2">
                    {e.profils?.map((prf, idx) => (
                        <span key={idx} className="px-2 py-1 bg-theme-bg border border-theme-border text-xs rounded-md text-theme-textSec font-medium">{prf.libelle || prf.code}</span>
                    ))}
                    {(!e.profils || e.profils.length === 0) && <span className="text-xs text-theme-textSec italic">Aucun profil spécifique rataché</span>}
                </div>
            </div>

            {(role === 'ADMIN') && (
                <div className="pt-4 border-t border-theme-border text-right mt-auto">
                    <button onClick={() => handleDelete(e.id)} className="text-red-500 hover:text-red-700 p-1.5 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/30 transition-colors">
                        <Trash2 size={18} />
                    </button>
                </div>
            )}
          </div>
        ))}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-theme-card rounded-2xl w-full max-w-md shadow-2xl border border-theme-border overflow-hidden flex flex-col max-h-[90vh]">
            <div className="flex justify-between items-center p-6 border-b border-theme-border shrink-0">
              <h2 className="text-xl font-bold text-theme-text">Ajout d'un Employé</h2>
              <button onClick={() => setIsModalOpen(false)} className="text-theme-textSec hover:text-theme-text cursor-pointer p-1 rounded-lg hover:bg-theme-bg transition-colors">
                <X size={24} />
              </button>
            </div>
            
            <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4 overflow-y-auto">
              <div className="grid grid-cols-2 gap-4">
                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-medium text-theme-text">Nom</label>
                  <input name="nom" value={formData.nom} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className="text-sm font-medium text-theme-text">Prénom</label>
                  <input name="prenom" value={formData.prenom} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
                </div>
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Email</label>
                <input name="email" type="email" value={formData.email} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Nom d'utilisateur (Login)</label>
                <input name="login" value={formData.login} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Mot de passe</label>
                <input name="password" type="password" value={formData.password} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Profil / Rôle initial</label>
                <select name="profilId" value={formData.profilId} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]">
                  <option value="">Sélectionner un profil</option>
                  {profils.map(p => <option key={p.id} value={p.id}>{p.libelle || p.code}</option>)}
                </select>
              </div>

              <div className="mt-4 flex gap-3 pb-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="flex-1 px-4 py-2.5 bg-theme-bg text-theme-text rounded-lg font-medium hover:opacity-80 transition-opacity border border-theme-border">
                  Annuler
                </button>
                <button type="submit" disabled={loading} className="flex-1 px-4 py-2.5 bg-theme-accent text-white rounded-lg font-medium hover:opacity-90 transition-opacity flex justify-center items-center">
                  {loading ? <Loader2 size={20} className="animate-spin" /> : 'Créer Compte'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}