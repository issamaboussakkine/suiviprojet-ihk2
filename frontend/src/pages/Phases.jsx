import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Phases() {
    const [phases, setPhases] = useState([]);
    const [projets, setProjets] = useState([]);
    const [formData, setFormData] = useState({
        nom: '', description: '', dateDebut: '', dateFin: '', montant: '', projet_id: ''
    });
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [phRes, prjRes] = await Promise.all([api.get('/phases'), api.get('/projets')]);
            setPhases(phRes.data);
            setProjets(prjRes.data);
        } catch (error) { console.error(error); }
    };

    const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault(); setLoading(true); setMessage('');
        try {
            await api.post('/phases', formData);
            setMessage('✅ Phase ajoutée !');
            setFormData({ nom: '', description: '', dateDebut: '', dateFin: '', montant: '', projet_id: '' });
            fetchData();
        } catch (error) {
            setMessage('❌ Erreur: ' + (error.response?.data?.message || 'Date hors limites ?'));
        } finally { setLoading(false); }
    };

    return (
        <div style={{ padding: '20px', background: '#0F172A', minHeight: '100vh', color: 'white' }}>
            <h1>Phases</h1>
            <form onSubmit={handleSubmit} style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
                <select name="projet_id" value={formData.projet_id} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }}>
                    <option value="">-- Choisir un projet --</option>
                    {projets.map(p => <option key={p.id} value={p.id}>{p.nom}</option>)}
                </select>
                <input name="nom" placeholder="Nom de la phase" value={formData.nom} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input name="description" placeholder="Description" value={formData.description} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input name="dateDebut" type="date" value={formData.dateDebut} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input name="dateFin" type="date" value={formData.dateFin} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input name="montant" type="number" placeholder="Montant (MAD)" value={formData.montant} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <button type="submit" disabled={loading} style={{ background: '#3B82F6', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px' }}>Ajouter</button>
            </form>
            {message && <p>{message}</p>}
            
            <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px' }}>
                <h3>Liste des phases</h3>
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    {phases.map(p => (
                        <li key={p.id} style={{ padding: '10px', borderBottom: '1px solid #334155' }}>
                            <strong>{p.nom}</strong> - {p.montant} MAD (Projet ID: {p.projet?.id})
                            <div>Statut: {p.etatRealisation ? 'Terminé' : 'En cours'}</div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
