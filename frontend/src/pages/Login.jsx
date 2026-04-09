import { useState } from 'react';
import { useAuthStore } from '../store/useAuthStore';

export default function Login() {
    const { login: setAuthStoreData } = useAuthStore();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage('Connexion en cours...');

        try {
            const response = await fetch('http://localhost:8081/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ login, password }),
            });

            const data = await response.json();

            if (response.ok && data.token) {
                const userData = {
                    id: data.id,
                    login: data.login,
                    nom: data.nom,
                    prenom: data.prenom,
                    role: data.role
                };
                localStorage.setItem('token', data.token);
                localStorage.setItem('user', JSON.stringify(userData));
                
                // Assurer la synchronisation avec Zustand pour que les routes protégées fonctionnent
                setAuthStoreData(userData, data.token);

                setMessage('✅ Connexion réussie ! Redirection...');
                setTimeout(() => {
                    window.location.href = '/dashboard';
                }, 1000);
            } else {
                setMessage('❌ ' + (data.message || data || 'Login ou mot de passe incorrect'));
                setLoading(false);
            }
        } catch (error) {
            console.error('Erreur de connexion:', error);
            setMessage('❌ Erreur de connexion au serveur. Vérifiez que le backend tourne sur http://localhost:8081');
            setLoading(false);
        }
    };

    return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh',
            background: 'linear-gradient(135deg, #0F172A 0%, #1E293B 100%)'
        }}>
            <form onSubmit={handleSubmit} style={{
                background: '#1E293B',
                padding: '40px',
                borderRadius: '16px',
                width: '380px',
                boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)'
            }}>
                <h2 style={{
                    color: '#F8FAFC',
                    textAlign: 'center',
                    marginBottom: '30px',
                    fontSize: '28px',
                    fontWeight: 'bold'
                }}>
                    IHKAPP
                </h2>

                <div style={{ marginBottom: '20px' }}>
                    <input
                        type="text"
                        placeholder="Login"
                        value={login}
                        onChange={(e) => setLogin(e.target.value)}
                        required
                        style={{
                            width: '100%',
                            padding: '12px',
                            borderRadius: '8px',
                            border: '1px solid #334155',
                            background: '#0F172A',
                            color: '#F8FAFC',
                            fontSize: '14px',
                            outline: 'none'
                        }}
                    />
                </div>

                <div style={{ marginBottom: '20px' }}>
                    <input
                        type="password"
                        placeholder="Mot de passe"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        style={{
                            width: '100%',
                            padding: '12px',
                            borderRadius: '8px',
                            border: '1px solid #334155',
                            background: '#0F172A',
                            color: '#F8FAFC',
                            fontSize: '14px',
                            outline: 'none'
                        }}
                    />
                </div>

                <button
                    type="submit"
                    disabled={loading}
                    style={{
                        width: '100%',
                        padding: '12px',
                        borderRadius: '8px',
                        border: 'none',
                        background: loading ? '#475569' : '#3B82F6',
                        color: 'white',
                        fontSize: '16px',
                        fontWeight: 'bold',
                        cursor: loading ? 'not-allowed' : 'pointer',
                        transition: 'background 0.3s'
                    }}
                >
                    {loading ? 'Connexion...' : 'Se connecter'}
                </button>

                {message && (
                    <p style={{
                        color: message.includes('✅') ? '#22C55E' : '#EF4444',
                        textAlign: 'center',
                        marginTop: '20px',
                        fontSize: '14px'
                    }}>
                        {message}
                    </p>
                )}
            </form>
        </div>
    );
}