import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Affectations() {
    const [affectations, setAffectations] = useState([]);
    const [employes, setEmployes] = useState([]);
    const [phases, setPhases] = useState([]);
    const [formData, setFormData] = useState({
        employeId: '',
        phaseId: '',
        dateDebut: '',
        dateFin: ''
    });
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchAffectations();
        fetchEmployes();
        fetchPhases();
    }, []);

    const fetchAffectations = async () => {
        try {
            const response = await api.get('/affectations');
            setAffectations(response.data);
        } catch (error) {
            console.error('Erreur chargement affectations:', error);
        }
    };

    const fetchEmployes = async () => {
        try {
            const response = await api.get('/employes');
            setEmployes(response.data);
        } catch (error) {
            console.error('Erreur chargement employés:', error);
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
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');
        try {
            await api.post('/affectations', formData);
            setMessage('✅ Affectation créée avec succès !');
            setFormData({ employeId: '', phaseId: '', dateDebut: '', dateFin: '' });
            fetchAffectations();
        } catch (error) {
            console.error('Erreur:', error);
            setMessage('❌ Erreur: ' + (error.response?.data?.message || error.message));
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (employeId, phaseId) => {
        if (!window.confirm('Supprimer cette affectation ?')) return;
        try {
            await api.delete(`/affectations/${employeId}/${phaseId}`);
            setMessage('✅ Affectation supprimée !');
            fetchAffectations();
        } catch (error) {
            setMessage('❌ Erreur lors de la suppression');
        }
    };

    // Fonction pour obtenir le texte d'affichage d'une phase
    const getPhaseDisplay = (phase) => {
        return phase.description || phase.libelle || phase.nom || `Phase ${phase.id}`;
    };

    // Fonction pour obtenir le texte d'affichage d'un employé
    const getEmployeDisplay = (emp) => {
        if (emp.nom && emp.prenom) return `${emp.nom} ${emp.prenom}`;
        if (emp.nom) return emp.nom;
        if (emp.login) return emp.login;
        return `Employé ${emp.id}`;
    };

    return (
        <div style={{ padding: '20px', background: '#0F172A', minHeight: '100vh', color: 'white' }}>
            <h1 style={{ marginBottom: '20px' }}>Affectations</h1>

            <form onSubmit={handleSubmit} style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
                <h3>Nouvelle affectation</h3>

                <select name="employeId" value={formData.employeId} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }}>
                    <option value="">-- Sélectionner un employé --</option>
                    {employes.map(emp => (
                        <option key={emp.id} value={emp.id}>
                            {getEmployeDisplay(emp)}
                        </option>
                    ))}
                </select>

                <select name="phaseId" value={formData.phaseId} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }}>
                    <option value="">-- Sélectionner une phase --</option>
                    {phases.map(phase => (
                        <option key={phase.id} value={phase.id}>
                            {getPhaseDisplay(phase)} - {phase.montant?.toLocaleString()} MAD
                        </option>
                    ))}
                </select>

                <input type="date" name="dateDebut" placeholder="Date début" value={formData.dateDebut} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />
                <input type="date" name="dateFin" placeholder="Date fin" value={formData.dateFin} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />

                <button type="submit" disabled={loading}
                    style={{ background: '#3B82F6', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px', cursor: 'pointer', marginTop: '10px' }}>
                    {loading ? 'Création...' : 'Créer l\'affectation'}
                </button>
            </form>

            {message && <p style={{ marginBottom: '20px', padding: '10px', borderRadius: '5px', background: message.includes('✅') ? '#22C55E' : '#EF4444' }}>{message}</p>}

            <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px' }}>
                <h3>Liste des affectations ({affectations.length})</h3>
                {affectations.length === 0 ? (
                    <p>Aucune affectation</p>
                ) : (
                    <ul style={{ listStyle: 'none', padding: 0 }}>
                        {affectations.map((aff, index) => (
                            <li key={index} style={{ padding: '10px', borderBottom: '1px solid #334155', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <span>
                                    <strong>{aff.employeNom || `Employé ${aff.employeId}`}</strong><br />
                                    📌 {aff.phaseLibelle || `Phase ${aff.phaseId}`}<br />
                                    📅 {aff.dateDebut} → {aff.dateFin}<br />
                                    🏢 {aff.projetNom || `Projet ID: ${aff.projetId || '?'}`}
                                </span>
                                <button onClick={() => handleDelete(aff.employeId, aff.phaseId)}
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