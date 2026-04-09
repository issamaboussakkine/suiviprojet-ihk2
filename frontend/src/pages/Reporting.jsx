import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Reporting() {
    const [stats, setStats] = useState({
        phasesTermineesNonFacturees: 0,
        phasesFactureesNonPayees: 0,
        projetsEnCours: 0,
        projetsClotures: 0
    });
    const [loading, setLoading] = useState(true);
    const [refresh, setRefresh] = useState(false);

    useEffect(() => {
        fetchStats();
    }, [refresh]);

    const fetchStats = async () => {
        setLoading(true);
        try {
            const [termineesRes, factureesRes, enCoursRes, cloturesRes] = await Promise.all([
                api.get('/reporting/phases/terminees-non-facturees').catch(() => ({ data: [] })),
                api.get('/reporting/phases/facturees-non-payees').catch(() => ({ data: [] })),
                api.get('/reporting/projets/en-cours').catch(() => ({ data: [] })),
                api.get('/reporting/projets/clotures').catch(() => ({ data: [] }))
            ]);

            setStats({
                phasesTermineesNonFacturees: termineesRes.data?.length || 0,
                phasesFactureesNonPayees: factureesRes.data?.length || 0,
                projetsEnCours: enCoursRes.data?.length || 0,
                projetsClotures: cloturesRes.data?.length || 0
            });
        } catch (error) {
            console.error('Erreur chargement reporting:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleRefresh = () => {
        setRefresh(!refresh);
    };

    if (loading) {
        return <div style={{ padding: '20px', color: 'white' }}>Chargement...</div>;
    }

    return (
        <div style={{ padding: '20px', background: '#0F172A', minHeight: '100vh', color: 'white' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                <h1>Reporting</h1>
                <button onClick={handleRefresh} style={{ background: '#3B82F6', color: 'white', padding: '8px 16px', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
                    🔄 Rafraîchir
                </button>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '20px' }}>
                <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', textAlign: 'center' }}>
                    <h2 style={{ fontSize: '36px', color: '#F59E0B' }}>{stats.phasesTermineesNonFacturees}</h2>
                    <p>Phases terminées non facturées</p>
                </div>
                <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', textAlign: 'center' }}>
                    <h2 style={{ fontSize: '36px', color: '#EF4444' }}>{stats.phasesFactureesNonPayees}</h2>
                    <p>Phases facturées non payées</p>
                </div>
                <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', textAlign: 'center' }}>
                    <h2 style={{ fontSize: '36px', color: '#10B981' }}>{stats.projetsEnCours}</h2>
                    <p>Projets en cours</p>
                </div>
                <div style={{ background: '#1E293B', padding: '20px', borderRadius: '8px', textAlign: 'center' }}>
                    <h2 style={{ fontSize: '36px', color: '#6366F1' }}>{stats.projetsClotures}</h2>
                    <p>Projets clôturés</p>
                </div>
            </div>
        </div>
    );
}