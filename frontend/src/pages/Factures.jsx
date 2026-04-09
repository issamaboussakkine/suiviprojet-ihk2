import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Factures() {
    const [factures, setFactures] = useState([]);
    const [phases, setPhases] = useState([]);
    const [formData, setFormData] = useState({
        phaseId: '',
        montant: '',
        dateEmission: ''
    });
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchFactures();
        fetchPhases();
    }, []);

    const fetchFactures = async () => {
        try {
            const response = await api.get('/factures');
            setFactures(response.data);
        } catch (error) {
            console.error('Erreur chargement factures:', error);
        }
    };

    const fetchPhases = async () => {
        try {
            const response = await api.get('/phases');
            // Filtrer les phases terminées et non facturées
            const phasesDisponibles = response.data.filter(p => p.etatRealisation && !p.etatFacturation);
            setPhases(phasesDisponibles);
        } catch (error) {
            console.error('Erreur chargement phases:', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });

        // Auto-remplir le montant quand une phase est sélectionnée
        if (name === 'phaseId') {
            const phase = phases.find(p => p.id == value);
            if (phase && phase.montant) {
                setFormData(prev => ({ ...prev, phaseId: value, montant: phase.montant }));
            }
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');
        try {
            await api.post('/factures', {
                phaseId: parseInt(formData.phaseId),
                montant: parseFloat(formData.montant),
                dateEmission: formData.dateEmission
            });
            setMessage('✅ Facture créée avec succès !');
            setFormData({ phaseId: '', montant: '', dateEmission: '' });
            fetchFactures();
            fetchPhases();
        } catch (error) {
            console.error('Erreur:', error);
            setMessage('❌ Erreur: ' + (error.response?.data?.message || error.message));
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Supprimer cette facture ?')) return;
        try {
            await api.delete(`/factures/${id}`);
            setMessage('✅ Facture supprimée !');
            fetchFactures();
        } catch (error) {
            setMessage('❌ Erreur lors de la suppression');
        }
    };

    const getPhaseDisplay = (phase) => {
        return phase.description || phase.libelle || phase.nom || `Phase ${phase.id}`;
    };

    const getStatutLabel = (facture) => {
        if (facture.etatPaiement) return 'Payée';
        if (facture.etatFacturation) return 'Facturée';
        return 'Non facturée';
    };

    const getStatutColor = (facture) => {
        if (facture.etatPaiement) return '#22C55E';
        if (facture.etatFacturation) return '#F59E0B';
        return '#EF4444';
    };

    return (
        <div style={{ padding: '20px', background: '#0F172A', minHeight: '100vh', color: 'white' }}>
            <h1 style={{ marginBottom: '20px' }}>Factures</h1>

            <form onSubmit={handleSubmit} style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
                <h3>Nouvelle facture</h3>

                <select name="phaseId" value={formData.phaseId} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }}>
                    <option value="">-- Sélectionner une phase terminée non facturée --</option>
                    {phases.map(phase => (
                        <option key={phase.id} value={phase.id}>
                            {getPhaseDisplay(phase)} - {phase.montant?.toLocaleString()} MAD
                        </option>
                    ))}
                </select>

                <input type="number" name="montant" placeholder="Montant" value={formData.montant} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />

                <input type="date" name="dateEmission" placeholder="Date d'émission" value={formData.dateEmission} onChange={handleChange} required
                    style={{ display: 'block', margin: '10px 0', padding: '8px', width: '100%', borderRadius: '5px' }} />

                <button type="submit" disabled={loading}
                    style={{ background: '#3B82F6', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px', cursor: 'pointer', marginTop: '10px' }}>
                    {loading ? 'Création...' : 'Créer la facture'}
                </button>
            </form>

            {message && <p style={{ marginBottom: '20px', padding: '10px', borderRadius: '5px', background: message.includes('✅') ? '#22C55E' : '#EF4444' }}>{message}</p>}

            <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px' }}>
                <h3>Liste des factures ({factures.length})</h3>
                {factures.length === 0 ? (
                    <p>Aucune facture</p>
                ) : (
                    <ul style={{ listStyle: 'none', padding: 0 }}>
                        {factures.map(f => (
                            <li key={f.id} style={{ padding: '10px', borderBottom: '1px solid #334155', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <span>
                                    <strong>Facture #{f.id}</strong><br />
                                    💰 Montant: {f.montant?.toLocaleString()} MAD<br />
                                    📅 Date: {f.dateEmission}<br />
                                    <span style={{ color: getStatutColor(f) }}>📌 Statut: {getStatutLabel(f)}</span><br />
                                    📌 Phase ID: {f.phaseId}
                                </span>
                                <button onClick={() => handleDelete(f.id)}
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