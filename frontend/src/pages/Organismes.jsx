import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuthStore } from '../store/useAuthStore';
import { Plus, X, Loader2, Building2, MapPin, Phone, Mail, Trash2 } from 'lucide-react';

export default function Organismes() {
  const { user } = useAuthStore();
  const role = user?.role;
  const [organismes, setOrganismes] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    nom: '', adresse: '', telephone: '', email_contact: ''
  });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);

  useEffect(() => {
    fetchOrganismes();
  }, []);

  const fetchOrganismes = async () => {
    try {
      const response = await api.get('/organismes');
      setOrganismes(response.data);
    } catch (error) {
      console.error('Erreur chargement:', error);
    } finally {
      setPageLoading(false);
    }
  };

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault(); setLoading(true); setMessage('');
    try {
      await api.post('/organismes', formData);
      setMessage('Organisme créé avec succès !');
      setFormData({ nom: '', adresse: '', telephone: '', email_contact: '' });
      setIsModalOpen(false);
      fetchOrganismes();
    } catch (error) {
      setMessage('Erreur: ' + (error.response?.data?.message || error.message));
    } finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer cet organisme ?')) return;
    try {
      await api.delete(`/organismes/${id}`);
      fetchOrganismes();
    } catch (err) { alert('Erreur lors de la suppression'); }
  };

  if (pageLoading) return <div className="flex justify-center p-12"><Loader2 className="animate-spin text-theme-accent" size={32} /></div>;

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-theme-text mb-1">Organismes Clients</h1>
          <p className="text-theme-textSec text-sm">Gestion des annonceurs et entités partenaires</p>
        </div>
        {(role === 'ADMIN' || role === 'SECRETAIRE') && (
          <button 
            onClick={() => setIsModalOpen(true)}
            className="flex items-center gap-2 bg-theme-accent text-white px-4 py-2 rounded-lg hover:opacity-90 transition-opacity font-medium"
          >
            <Plus size={20} /> Nouvel Organisme
          </button>
        )}
      </div>

      {message && <div className="p-4 bg-theme-card border border-theme-border text-theme-text font-medium rounded-lg">{message}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {organismes.map(o => (
          <div key={o.id} className="bg-theme-card rounded-xl border border-theme-border p-5 flex flex-col justify-between transition-colors duration-200">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-xl bg-theme-bg border border-theme-border flex items-center justify-center shrink-0">
                    <Building2 size={24} className="text-theme-accent" />
                </div>
                <div>
                  <h3 className="text-lg font-bold text-theme-text">{o.nom}</h3>
                </div>
              </div>
            </div>

            <div className="space-y-3 mb-4 flex-1">
                <div className="flex items-start gap-2 text-sm text-theme-text">
                    <MapPin size={16} className="text-theme-textSec shrink-0 mt-0.5" />
                    <span className="line-clamp-2">{o.adresse || 'Aucune adresse renseignée'}</span>
                </div>
                <div className="flex items-center gap-2 text-sm text-theme-text">
                    <Phone size={16} className="text-theme-textSec shrink-0" />
                    <span>{o.telephone || 'Non renseigné'}</span>
                </div>
                <div className="flex items-center gap-2 text-sm text-theme-text">
                    <Mail size={16} className="text-theme-textSec shrink-0" />
                    <span className="truncate">{o.email_contact || 'Non renseigné'}</span>
                </div>
            </div>

            {(role === 'ADMIN') && (
                <div className="pt-4 border-t border-theme-border text-right mt-auto">
                    <button onClick={() => handleDelete(o.id)} className="text-red-500 hover:text-red-700 p-1.5 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/30 transition-colors">
                        <Trash2 size={18} />
                    </button>
                </div>
            )}
          </div>
        ))}
        {organismes.length === 0 && (
            <div className="col-span-full p-8 text-center text-theme-textSec">Aucun organisme enregistré.</div>
        )}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-theme-card rounded-2xl w-full max-w-md shadow-2xl border border-theme-border overflow-hidden flex flex-col max-h-[90vh]">
            <div className="flex justify-between items-center p-6 border-b border-theme-border shrink-0">
              <h2 className="text-xl font-bold text-theme-text">Créer Organisme</h2>
              <button onClick={() => setIsModalOpen(false)} className="text-theme-textSec hover:text-theme-text cursor-pointer p-1 rounded-lg hover:bg-theme-bg transition-colors">
                <X size={24} />
              </button>
            </div>
            
            <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4 overflow-y-auto">
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Raison sociale / Nom</label>
                <input name="nom" value={formData.nom} onChange={handleChange} required className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Adresse</label>
                <textarea name="adresse" value={formData.adresse} onChange={handleChange} className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark] min-h-[60px]" />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Téléphone</label>
                <input name="telephone" value={formData.telephone} onChange={handleChange} className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-theme-text">Email de contact</label>
                <input name="email_contact" type="email" value={formData.email_contact} onChange={handleChange} className="w-full p-2.5 rounded-lg bg-theme-bg border border-theme-border text-theme-text focus:outline-none focus:border-theme-accent [color-scheme:light_dark]" />
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