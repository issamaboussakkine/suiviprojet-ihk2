import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Documents() {
    const [documents, setDocuments] = useState([]);
    const [projets, setProjets] = useState([]);
    const [formData, setFormData] = useState({
        projetId: '',
        code: '',
        libelle: '',
        description: '',
        fichier: null
    });
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchDocuments();
        fetchProjets();
    }, []);

    const fetchDocuments = async () => {
        try {
            const response = await api.get('/documents');
            setDocuments(response.data);
        } catch (error) {
            console.error('Erreur chargement documents:', error);
        }
    };

    const fetchProjets = async () => {
        try {
            const response = await api.get('/projets');
            setProjets(response.data);
        } catch (error) {
            console.error('Erreur chargement projets:', error);
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
            data.append('projetId', formData.projetId);
            data.append('code', formData.code);
            data.append('libelle', formData.libelle);
            data.append('description', formData.description);
            if (formData.fichier) {
                data.append('fichier', formData.fichier);
            }

            await api.post('/documents/upload', data, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            setMessage('✅ Document créé avec succès !');
            setFormData({ projetId: '', code: '', libelle: '', description: '', fichier: null });
            fetchDocuments();
        } catch (error) {
            setMessage('❌ Erreur: ' + (error.response?.data?.message || error.message));
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Supprimer ce document ?')) return;
        try {
            await api.delete(`/documents/${id}`);
            fetchDocuments();
        } catch (error) {
            setMessage('❌ Erreur lors de la suppression');
        }
    };

    return (
        <div style={{ padding: '20px', background: '#0F172A', minHeight: '100vh', color: 'white' }}>
            <h1>Documents</h1>

            <form onSubmit={handleSubmit} style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
                <h3>Nouveau document</h3>

                <select name="projetId" value={formData.projetId} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }}>
                    <option value="">-- Sélectionner un projet --</option>
                    {projets.map(p => (
                        <option key={p.id} value={p.id}>{p.nom} ({p.code})</option>
                    ))}
                </select>

                <input name="code" placeholder="Code" value={formData.code} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />
                <input name="libelle" placeholder="Libellé" value={formData.libelle} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />
                <textarea name="description" placeholder="Description" value={formData.description} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />

                <input type="file" name="fichier" onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />

                <button type="submit" disabled={loading} style={{ background: '#3B82F6', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px', marginTop: '10px' }}>
                    {loading ? 'Envoi...' : 'Créer le document'}
                </button>
            </form>

            {message && <p style={{ color: message.includes('✅') ? '#22C55E' : '#EF4444' }}>{message}</p>}

            <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px' }}>
                <h3>Liste des documents</h3>
                {documents.map(d => (
                    <li key={d.id} style={{ padding: '10px', borderBottom: '1px solid #334155' }}>
                        {d.libelle} - {d.description}
                        <button onClick={() => handleDelete(d.id)} style={{ background: '#EF4444', color: 'white', border: 'none', padding: '5px 10px', borderRadius: '5px', marginLeft: '10px' }}>Supprimer</button>
                    </li>
                ))}
            </div>
        </div>
    );
}