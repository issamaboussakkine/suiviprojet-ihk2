import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Employes() {
    const [employes, setEmployes] = useState([]);
    const [profils, setProfils] = useState([]);
    const [formData, setFormData] = useState({
        nom: '',
        prenom: '',
        email: '',
        login: '',
        password: '',
        profilId: ''
    });
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

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

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');
        try {
            await api.post('/employes', formData);
            setMessage('✅ Employé créé avec succès !');
            setFormData({ nom: '', prenom: '', email: '', login: '', password: '', profilId: '' });
            fetchEmployes();
        } catch (error) {
            setMessage('❌ Erreur: ' + (error.response?.data?.message || error.message));
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Supprimer cet employé ?')) return;
        try {
            await api.delete(`/employes/${id}`);
            fetchEmployes();
        } catch (error) {
            setMessage('❌ Erreur lors de la suppression');
        }
    };

    return (
        <div style={{ padding: '20px', background: '#0F172A', minHeight: '100vh', color: 'white' }}>
            <h1>Employés</h1>

            <form onSubmit={handleSubmit} style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
                <h3>Nouvel employé</h3>
                <input name="nom" placeholder="Nom" value={formData.nom} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />
                <input name="prenom" placeholder="Prénom" value={formData.prenom} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />
                <input name="email" placeholder="Email" value={formData.email} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />
                <input name="login" placeholder="Login" value={formData.login} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />
                <input name="password" type="password" placeholder="Mot de passe" value={formData.password} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }} />

                <select name="profilId" value={formData.profilId} onChange={handleChange} required style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%' }}>
                    <option value="">-- Sélectionner un profil --</option>
                    {profils.map(p => (
                        <option key={p.id} value={p.id}>{p.libelle || p.code}</option>
                    ))}
                </select>

                <button type="submit" disabled={loading} style={{ background: '#3B82F6', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px', marginTop: '10px' }}>
                    {loading ? 'Enregistrement...' : 'Enregistrer'}
                </button>
            </form>

            {message && <p style={{ color: message.includes('✅') ? '#22C55E' : '#EF4444' }}>{message}</p>}

            <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px' }}>
                <h3>Liste des employés</h3>
                {employes.map(e => (
                    <li key={e.id} style={{ padding: '10px', borderBottom: '1px solid #334155' }}>
                        {e.nom} {e.prenom} - {e.email} - {e.login}
                        <button onClick={() => handleDelete(e.id)} style={{ background: '#EF4444', color: 'white', border: 'none', padding: '5px 10px', borderRadius: '5px', marginLeft: '10px' }}>Supprimer</button>
                    </li>
                ))}
            </div>
        </div>
    );
}