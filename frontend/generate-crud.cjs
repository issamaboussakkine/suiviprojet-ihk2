const fs = require('fs');
const path = require('path');

const generateCrud = (name, serviceName, modelName, columns) => `
import React, { useEffect, useState } from 'react';
import { ${serviceName} } from '../services/apiServices';
import DataTable from '../components/DataTable';
import { Plus, Loader2 } from 'lucide-react';
import { useForm } from 'react-hook-form';

const ${name} = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const { register, handleSubmit, reset } = useForm();

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await ${serviceName}.getAll();
      setData(Array.isArray(response) ? response : (response.content || []));
    } catch (error) { console.error('Error', error); } finally { setLoading(false); }
  };

  useEffect(() => { loadData(); }, []);

  const onSubmit = async (formData) => {
    try {
      if (editingItem) await ${serviceName}.update(editingItem.id, formData);
      else await ${serviceName}.create(formData);
      setIsModalOpen(false); reset(); loadData();
    } catch (error) { console.error(error); }
  };

  const handleEdit = (item) => { setEditingItem(item); reset(item); setIsModalOpen(true); };

  const handleDelete = async (item) => {
    if (window.confirm('Voulez-vous supprimer cet élément ?')) {
      try { await ${serviceName}.delete(item.id); loadData(); } catch(e) { console.error(e); }
    }
  };

  const columns = ${JSON.stringify(columns)};

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-50">${name}</h1>
          <p className="text-slate-400">Gestion des ${modelName}</p>
        </div>
        <button onClick={() => { setEditingItem(null); reset({}); setIsModalOpen(true); }} className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-xl flex items-center gap-2">
          <Plus size={20} /> <span>Nouveau</span>
        </button>
      </div>
      {loading ? <div className="flex justify-center p-12"><Loader2 className="animate-spin text-blue-500" size={32} /></div> : <DataTable columns={columns} data={data} onEdit={handleEdit} onDelete={handleDelete} />}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="bg-slate-800 rounded-2xl p-6 border border-slate-700 w-full max-w-lg">
            <h2 className="text-xl font-bold text-slate-100 mb-6">{editingItem ? 'Modifier' : 'Nouveau'}</h2>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              {columns.map(col => col.accessor !== 'id' && (
                <div key={col.accessor}>
                  <label className="block text-sm text-slate-300 mb-1">{col.header}</label>
                  <input {...register(col.accessor)} className="w-full bg-slate-900 border border-slate-700 rounded-xl px-4 py-2 text-slate-100 outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
              ))}
              <div className="flex justify-end gap-3 mt-6 pt-4 border-t border-slate-700">
                <button type="button" onClick={() => setIsModalOpen(false)} className="px-4 py-2 rounded-xl text-slate-300 hover:bg-slate-700 transition-colors">Annuler</button>
                <button type="submit" className="px-4 py-2 rounded-xl bg-blue-600 text-white hover:bg-blue-700">Sauvegarder</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
export default ${name};
`;

const pages = [
  { name: 'Employes', service: 'employeService', model: 'employés', cols: [{header:'ID',accessor:'id'},{header:'Nom',accessor:'nom'},{header:'Prénom',accessor:'prenom'},{header:'Email',accessor:'email'},{header:'Role',accessor:'role'}] },
  { name: 'Projets', service: 'projetService', model: 'projets', cols: [{header:'ID',accessor:'id'},{header:'Titre',accessor:'titre'},{header:'Statut',accessor:'statut'},{header:'Budget',accessor:'budget'}] },
  { name: 'Phases', service: 'phaseService', model: 'phases', cols: [{header:'ID',accessor:'id'},{header:'Titre',accessor:'titre'},{header:'État',accessor:'etat'},{header:'Projet ID',accessor:'projetId'}] },
  { name: 'Affectations', service: 'affectationService', model: 'affectations', cols: [{header:'ID',accessor:'id'},{header:'Employé ID',accessor:'employeId'},{header:'Phase ID',accessor:'phaseId'},{header:'Date Début',accessor:'dateDebut'},{header:'Date Fin',accessor:'dateFin'}] },
  { name: 'Livrables', service: 'livrableService', model: 'livrables', cols: [{header:'ID',accessor:'id'},{header:'Nom',accessor:'nom'},{header:'Statut',accessor:'statut'},{header:'Phase ID',accessor:'phaseId'}] },
  { name: 'Documents', service: 'documentService', model: 'documents', cols: [{header:'ID',accessor:'id'},{header:'Nom Fichier',accessor:'nomFichier'},{header:'Type',accessor:'typeDocument'}] },
  { name: 'Factures', service: 'factureService', model: 'factures', cols: [{header:'ID',accessor:'id'},{header:'Montant',accessor:'montant'},{header:'Date',accessor:'dateCreation'},{header:'État',accessor:'etat'}] },
  { name: 'Reporting', service: 'dashboardService', model: 'reporting', cols: [{header:'ID',accessor:'id'}] }
];

pages.forEach(p => {
  if (p.name !== 'Reporting') {
      fs.writeFileSync(path.join('c:/Users/ASUS/OneDrive/suiviprojet-ihk2/frontend/src/pages', p.name + '.jsx'), generateCrud(p.name, p.service, p.model, p.cols));
  }
});
console.log('Pages générées avec succès !');
