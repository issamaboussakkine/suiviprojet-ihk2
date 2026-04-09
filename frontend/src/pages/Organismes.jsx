import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Organismes() {
    const [organismes, setOrganismes] = useState([]);
    const [formData, setFormData] = useState({
        nom: '',
        adresse: '',
        telephone: '',
        email_contact: ''
    });
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchOrganismes();
    }, []);

    const fetchOrganismes = async () => {
        try {
            const response = await api.get('/organismes');
            setOrganismes(response.data);
        } catch (error) {
            console.error('Erreur chargement:', error);
        }
    };

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');
        try {
            await api.post('/organismes', formData);
            setMessage('✅ Organisme créé avec succès !');
            setFormData({ nom: '', adresse: '', telephone: '', email_contact: '' });
            fetchOrganismes();
        } catch (error) {
            console.error('Erreur:', error);
            setMessage('❌ Erreur: ' + (error.response?.data?.message || error.message));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ padding: '20px', background: '#0F172A', minHeight: '100vh', color: 'white' }}>
            <h1 style={{ marginBottom: '20px' }}>Organismes</h1>
            
            <form onSubmit={handleSubmit} style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
                <h3>Nouvel Organisme</h3>
                <input name="nom" placeholder="Nom de l'organisme" value={formData.nom} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px', border: 'none' }} />
                <input name="adresse" placeholder="Adresse" value={formData.adresse} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px', border: 'none' }} />
                <input name="telephone" placeholder="Téléphone" value={formData.telephone} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px', border: 'none' }} />
                <input name="email_contact" placeholder="Email de contact" type="email" value={formData.email_contact} onChange={handleChange} style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px', border: 'none' }} />
                <button type="submit" disabled={loading} style={{ background: '#3B82F6', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
                    {loading ? 'Enregistrement...' : 'Enregistrer'}
                </button>
            </form>
            
            {message && <p style={{ marginBottom: '20px' }}>{message}</p>}
            
            <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px' }}>
                <h3>Liste des organismes</h3>
                {organismes.length === 0 ? (
                    <p>Aucun organisme</p>
                ) : (
                    <ul style={{ listStyle: 'none', padding: 0 }}>
                        {organismes.map(o => (
                            <li key={o.id} style={{ padding: '10px', borderBottom: '1px solid #334155' }}>
                                <strong>{o.nom}</strong> - {o.email_contact}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
}