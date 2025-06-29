# Example Python Code to Insert a Document
from pymongo import MongoClient
from bson.objectid import ObjectId

class AnimalShelter(object):
    
    "Two extra parameters are added to the constructor to allow users to authenticate when creating a class instance"
    def __init__(self, USER, PASS): 
        # Initializing the MongoClient. This helps to 
        # access the MongoDB databases and collections.
        # This is hard-wired to use the aac database, the 
        # animals collection, and the aac user.
        # Definitions of the connection string variables are
        # unique to the individual Apporto environment.
        #
        # You must edit the connection variables below to reflect
        # your own instance of MongoDB!
        #
        # Connection Variables
        HOST = 'nv-desktop-services.apporto.com'
        PORT = 33737
        DB = 'AAC'
        COL = 'animals'
        
        # Initialize Connection
        try:
            # Credentials to establish the connection
            self.client = MongoClient('mongodb://%s:%s@%s:%d' % (USER, PASS, HOST, PORT))
            
            # Assigned DB and Collection to the class 
            self.database = self.client[DB]
            self.collection = self.database[COL]
            
            # If the info is authenticated then message will be printed
            if self.client.server_info():
                print("MongoDB connection Established...")

        # Otherwise, an error regarding the connection will be thrown
        except Exception as e:
            print(f"Connection cannot be established: {e}")
        
    def create(self, data):
        """
        Inserta un documento en la colección 'animals'.
        """
        if data is not None:
            # Usamos insert_one en lugar de insert (ya que este último está deprecado)
            result = self.collection.insert_one(data)
            return result.inserted_id is not None
        else:
            raise Exception("Nothing to save, because data parameter is empty")
    
    def read(self, criteria=None):
        """
        Lee documentos de la colección 'animals' basándose en el criterio 'criteria'.
        Retorna una lista de documentos.
        """
        if criteria is None:
            criteria = {}
        # Se realiza la consulta a la colección
        data = self.collection.find(criteria, {"_id": False})
        # Convertimos el cursor a una lista
        data_list = list(data)
        # Se imprime cada documento para depuración
        for document in data_list:
            print(document)
        return data_list
    
    def update(self, search, newData):
        """
        Actualiza documentos que coincidan con 'search' estableciendo los campos de 'newData'.
        Retorna el número de documentos modificados.
        """
        if search and newData is not None:
            result = self.collection.update_many(search, {"$set": newData})
            return result.modified_count
        else:
            raise Exception("Search criteria or new data is empty")
    
    def delete(self, deleteData):
        """
        Elimina documentos que coincidan con 'deleteData'.
        Retorna el número de documentos eliminados.
        """
        if deleteData is not None:
            result = self.collection.delete_many(deleteData)
            print("Documentos eliminados:", result.deleted_count)
            return result.deleted_count
        else:
            raise Exception("Nothing to delete, because data parameter is empty")
