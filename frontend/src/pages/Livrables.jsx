import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Livrables() {
    const [livrables, setLivrables] = useState([]);
    const [phases, setPhases] = useState([]);
    const [formData, setFormData] = useState({
        phaseId: '',
        code: '',
        libelle: '',
        description: '',
        fichier: null
    });
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

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
        }
    };

    const fetchPhases = async () => {
        try {
            const response = await api.get('/phases');
            setPhases(response.data);
        } catch (error) {
            console.error('Erreur chargement phases:', error);
        }
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
        e.preventDefault();
        setLoading(true);
        setMessage('');
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
            setMessage('✅ Livrable créé avec succès !');
            setFormData({ phaseId: '', code: '', libelle: '', description: '', fichier: null });
            fetchLivrables();
        } catch (error) {
            console.error('Erreur:', error);
            setMessage('❌ Erreur: ' + (error.response?.data?.message || error.message));
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Supprimer ce livrable ?')) return;
        try {
            await api.delete(`/livrables/${id}`);
            setMessage('✅ Livrable supprimé !');
            fetchLivrables();
        } catch (error) {
            setMessage('❌ Erreur lors de la suppression');
        }
    };

    const getPhaseDisplay = (phase) => {
        return phase.description || phase.libelle || phase.nom || `Phase ${phase.id}`;
    };

    return (
        <div style={{ padding: '20px', background: '#0F172A', minHeight: '100vh', color: 'white' }}>
            <h1>Livrables</h1>

            <form onSubmit={handleSubmit} style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
                <h3>Nouveau livrable</h3>

                <select name="phaseId" value={formData.phaseId} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }}>
                    <option value="">-- Sélectionner une phase --</option>
                    {phases.map(phase => (
                        <option key={phase.id} value={phase.id}>
                            {getPhaseDisplay(phase)} - {phase.montant?.toLocaleString()} MAD
                        </option>
                    ))}
                </select>

                <input name="code" placeholder="Code" value={formData.code} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input name="libelle" placeholder="Libellé" value={formData.libelle} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <textarea name="description" placeholder="Description" value={formData.description} onChange={handleChange}
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px', minHeight: '80px' }} />
                <input type="file" name="fichier" onChange={handleChange}
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />

                <button type="submit" disabled={loading}
                    style={{ background: '#3B82F6', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px', cursor: 'pointer', marginTop: '10px' }}>
                    {loading ? 'Création...' : 'Créer le livrable'}
                </button>
            </form>

            {message && <p style={{ color: message.includes('✅') ? '#22C55E' : '#EF4444' }}>{message}</p>}

            <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px' }}>
                <h3>Liste des livrables ({livrables.length})</h3>
                {livrables.length === 0 ? (
                    <p>Aucun livrable</p>
                ) : (
                    <ul style={{ listStyle: 'none', padding: 0 }}>
                        {livrables.map(l => (
                            <li key={l.id} style={{ padding: '10px', borderBottom: '1px solid #334155', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <span>
                                    <strong>{l.libelle || l.code}</strong><br />
                                    📝 {l.description}<br />
                                    📌 Phase ID: {l.phaseId}
                                    {l.chemin && <span><br />📎 <a href="#" onClick={() => window.open(`http://localhost:8081/api/livrables/download/${l.id}`)} style={{ color: '#3B82F6' }}>Télécharger</a></span>}
                                </span>
                                <button onClick={() => handleDelete(l.id)}
                                    style={{ background: '#EF4444', color: 'white', border: 'none', padding: '5px 10px', borderRadius: '5px', cursor: 'pointer' }}>
                                    Supprimer
                                </button>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
}