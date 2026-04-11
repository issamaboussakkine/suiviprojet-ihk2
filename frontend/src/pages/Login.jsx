import { useState } from 'react';
import { useAuthStore } from '../store/useAuthStore';
import { Loader2, KanbanSquare, Users, Activity, BarChart3, Briefcase } from 'lucide-react';

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
                // Extraction robuste du code du rôle (chaîne de caractères) pour éviter "Objects are not valid as a React child"
                let roleString = 'USER';
                if (data.role) {
                    if (typeof data.role === 'object') {
                        roleString = data.role.code || data.role.libelle || 'USER';
                    } else {
                        roleString = data.role;
                    }
                }
                roleString = roleString.toUpperCase();

                const userData = {
                    id: data.id,
                    login: data.login,
                    nom: data.nom,
                    prenom: data.prenom,
                    role: roleString
                };
                localStorage.setItem('token', data.token);
                localStorage.setItem('user', JSON.stringify(userData));
                
                // Assurer la synchronisation avec Zustand pour que les routes protégées fonctionnent
                setAuthStoreData(userData, data.token);

                setMessage('Connexion réussie ! Redirection...');
                setTimeout(() => {
                    window.location.href = '/dashboard';
                }, 1000);
            } else {
                setMessage(data.message || data || 'Login ou mot de passe incorrect');
                setLoading(false);
            }
        } catch (error) {
            console.error('Erreur de connexion:', error);
            setMessage('Erreur de connexion au serveur. Vérifiez que le backend est lancé.');
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen grid grid-cols-1 md:grid-cols-2 bg-theme-bg text-theme-text font-sans">
            {/* Left Column: Form */}
            <div className="flex flex-col justify-center items-center p-8 sm:p-12 lg:p-24 w-full h-full">
                <div className="w-full max-w-md animate-fade-in">
                    <div className="mb-10 text-center md:text-left">
                        <h2 className="text-4xl font-black text-theme-text mb-2 tracking-tight">IHKAPP</h2>
                        <p className="text-theme-textSec font-medium">Connectez-vous pour gérer vos projets.</p>
                    </div>

                    <form onSubmit={handleSubmit} className="bg-theme-card p-8 rounded-2xl shadow-xl border border-theme-border/50 backdrop-blur-xl">
                        <div className="space-y-5">
                            <div>
                                <label className="block text-sm font-bold text-theme-text mb-1.5 ml-1">Identifiant</label>
                                <input
                                    type="text"
                                    placeholder="Entrez votre login"
                                    value={login}
                                    onChange={(e) => setLogin(e.target.value)}
                                    required
                                    className="w-full p-3.5 rounded-xl bg-theme-bg border-2 border-theme-border focus:border-theme-accent focus:ring-4 focus:ring-theme-accent/20 outline-none transition-all placeholder:text-theme-textSec/60 font-medium"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-bold text-theme-text mb-1.5 ml-1">Mot de passe</label>
                                <input
                                    type="password"
                                    placeholder="••••••••"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                    className="w-full p-3.5 rounded-xl bg-theme-bg border-2 border-theme-border focus:border-theme-accent focus:ring-4 focus:ring-theme-accent/20 outline-none transition-all placeholder:text-theme-textSec/60 font-medium"
                                />
                            </div>

                            <button
                                type="submit"
                                disabled={loading}
                                className="w-full mt-4 p-3.5 rounded-xl bg-theme-accent text-white font-bold text-lg hover:opacity-90 transition-opacity disabled:opacity-70 disabled:cursor-not-allowed flex justify-center items-center gap-2 group shadow-md shadow-theme-accent/20"
                            >
                                {loading ? (
                                    <>
                                        <Loader2 className="animate-spin" size={24} />
                                        <span>Connexion...</span>
                                    </>
                                ) : (
                                    <span>Se connecter</span>
                                )}
                            </button>
                        </div>

                        {message && (
                            <div className={`mt-6 p-4 rounded-xl text-sm font-medium border text-center ${message.includes('réussie') ? 'bg-green-100/50 text-green-700 border-green-200 dark:bg-green-900/30 dark:text-green-400 dark:border-green-800' : 'bg-red-100/50 text-red-700 border-red-200 dark:bg-red-900/30 dark:text-red-400 dark:border-red-800'}`}>
                                {message}
                            </div>
                        )}
                    </form>
                    
                    <p className="mt-8 text-center text-sm font-semibold text-theme-textSec">
                        Système de Gestion de Projet IHK
                    </p>
                </div>
            </div>

            {/* Right Column: Animated Visual */}
            <div className="hidden md:flex relative flex-col justify-center items-center bg-theme-card border-l border-theme-border overflow-hidden">
                {/* Decorative Background Elements */}
                <div className="absolute top-[-10%] left-[-10%] w-96 h-96 bg-theme-accent/20 rounded-full blur-3xl opacity-50 mix-blend-multiply dark:mix-blend-lighten animate-pulse" style={{ animationDuration: '4s' }}></div>
                <div className="absolute bottom-[-10%] right-[-10%] w-[30rem] h-[30rem] bg-theme-accent/20 rounded-full blur-3xl opacity-50 mix-blend-multiply dark:mix-blend-lighten animate-pulse" style={{ animationDuration: '6s', animationDelay: '1s' }}></div>

                {/* Animated Floating Abstract Dashboard Representation */}
                <div className="relative w-full max-w-md h-[500px] flex items-center justify-center pointer-events-none">
                    
                    {/* Center piece */}
                    <div className="absolute z-10 flex items-center justify-center w-32 h-32 bg-theme-bg rounded-3xl shadow-2xl border-4 border-theme-accent animate-bounce" style={{ animationDuration: '3s' }}>
                        <KanbanSquare size={64} className="text-theme-accent" />
                    </div>

                    {/* Orbiting piece 1 */}
                    <div className="absolute flex items-center justify-center w-16 h-16 bg-white dark:bg-[#2A2420] rounded-2xl shadow-xl shadow-theme-accent/10 border border-theme-border/50 animate-[spin_8s_linear_infinite]" style={{ transformOrigin: '200px 0' }}>
                        <div className="animate-[spin_8s_linear_infinite_reverse]">
                            <Users size={28} className="text-[#8B7355]" />
                        </div>
                    </div>

                    {/* Orbiting piece 2 */}
                    <div className="absolute flex items-center justify-center w-20 h-20 bg-theme-bg rounded-2xl shadow-xl shadow-theme-accent/10 border border-theme-accent/30 animate-[spin_12s_linear_infinite]" style={{ transformOrigin: '-180px 100px' }}>
                        <div className="animate-[spin_12s_linear_infinite_reverse]">
                            <Activity size={32} className="text-[#D9A299]" />
                        </div>
                    </div>

                    {/* Orbiting piece 3 */}
                    <div className="absolute flex items-center justify-center w-14 h-14 bg-white dark:bg-[#2A2420] rounded-xl shadow-xl shadow-theme-accent/10 border border-theme-border/50 animate-[spin_10s_linear_infinite]" style={{ transformOrigin: '150px -150px' }}>
                        <div className="animate-[spin_10s_linear_infinite_reverse]">
                            <BarChart3 size={24} className="text-[#5C4B3A] dark:text-[#F0E4D3]" />
                        </div>
                    </div>
                    
                    {/* Orbiting piece 4 */}
                    <div className="absolute flex items-center justify-center w-16 h-16 bg-theme-bg rounded-2xl shadow-xl shadow-theme-accent/10 border border-theme-accent/30 animate-[spin_15s_linear_infinite]" style={{ transformOrigin: '-100px -200px' }}>
                        <div className="animate-[spin_15s_linear_infinite_reverse]">
                            <Briefcase size={28} className="text-[#D9A299]" />
                        </div>
                    </div>

                    {/* Connecting lines abstraction */}
                    <div className="absolute w-80 h-80 border-[0.5px] border-theme-border rounded-full opacity-50 border-dashed animate-[spin_20s_linear_infinite]"></div>
                    <div className="absolute w-[450px] h-[450px] border-[0.5px] border-theme-accent/30 rounded-full opacity-30 border-dashed animate-[spin_25s_linear_infinite_reverse]"></div>
                </div>

                <div className="absolute bottom-12 text-center">
                    <h3 className="text-xl font-bold text-theme-text mb-2">Collaboration Simplifiée</h3>
                    <p className="text-theme-textSec text-sm px-8 max-w-sm">
                        Suivez l'avancement de vos projets, gérez vos équipes et centralisez vos documents en seul endroit.
                    </p>
                </div>
            </div>
        </div>
    );
}