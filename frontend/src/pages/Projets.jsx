import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Projets() {
    const [projets, setProjets] = useState([]);
    const [organismes, setOrganismes] = useState([]);
    const [employes, setEmployes] = useState([]);
    const [formData, setFormData] = useState({
        nom: '', code: '', dateDebut: '', dateFin: '', montant: '', organisme_id: '', chef_projet_id: ''
    });
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

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
        }
    };

    const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true); setMessage('');
        try {
            await api.post('/projets', formData);
            setMessage('✅ Projet créé avec succès !');
            setFormData({ nom: '', code: '', dateDebut: '', dateFin: '', montant: '', organisme_id: '', chef_projet_id: '' });
            fetchData();
        } catch (error) {
            setMessage('❌ Erreur: ' + (error.response?.data?.message || error.message));
        } finally { setLoading(false); }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Supprimer ce projet ?')) return;
        try {
            await api.delete(`/projets/${id}`);
            fetchData();
        } catch (err) { alert('Erreur lors de la suppression'); }
    };

    return (
        <div style={{ padding: '20px', background: '#0F172A', minHeight: '100vh', color: 'white' }}>
            <h1 style={{ marginBottom: '20px' }}>Projets</h1>
            
            <form onSubmit={handleSubmit} style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
                <h3>Nouveau Projet</h3>
                <input name="nom" placeholder="Nom du projet" value={formData.nom} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input name="code" placeholder="Code" value={formData.code} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input name="dateDebut" type="date" value={formData.dateDebut} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input name="dateFin" type="date" value={formData.dateFin} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input name="montant" type="number" placeholder="Montant (MAD)" value={formData.montant} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                
                <select name="organisme_id" value={formData.organisme_id} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }}>
                    <option value="">Sélectionner un organisme</option>
                    {organismes.map(o => <option key={o.id} value={o.id}>{o.nom}</option>)}
                </select>

                <select name="chef_projet_id" value={formData.chef_projet_id} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }}>
                    <option value="">Sélectionner un chef de projet</option>
                    {employes.map(e => <option key={e.id} value={e.id}>{e.nom} {e.prenom}</option>)}
                </select>

                <button type="submit" disabled={loading} style={{ background: '#3B82F6', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
                    {loading ? 'Enregistrement...' : 'Enregistrer'}
                </button>
            </form>
            {message && <p>{message}</p>}
            
            <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px' }}>
                <h3>Liste des projets</h3>
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    {projets.map(p => (
                        <li key={p.id} style={{ padding: '10px', borderBottom: '1px solid #334155', display: 'flex', justifyContent: 'space-between' }}>
                            <div><strong>{p.nom} ({p.code})</strong> - {p.montant} MAD</div>
                            <button onClick={() => handleDelete(p.id)} style={{ background: 'red', color: 'white', border: 'none', padding: '5px 10px', borderRadius: '5px', cursor: 'pointer' }}>Supprimer</button>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
