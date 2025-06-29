# Methods / library imports
from pymongo import MongoClient

class DBConnection(object):
    
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

        """
        For code review purpose I replace the external connection from the previous class because
        is no longer available. I installed mongoDB locally to test the program
        old parameters below: 
        HOST = 'nv-desktop-services.apporto.com' PORT = 33737 DB = 'AAC' COL = 'animals'
        """

        # New Parameters 
        HOST = 'cluster0.phm9fk9'
        # PORT = 27017
        DB = 'AAC'
        COL = 'animals'
        
        # Initialize Connection
        try:
            # mongo atlas uri
            uri = f"mongodb+srv://{USER}:{PASS}@{HOST}.mongodb.net/?retryWrites=true&w=majority"
            # Credentials to establish the connection
            # %s for string %d for int
            self.client = MongoClient(uri)
            
            # Assigned DB and Collection to the class 
            self.database = self.client[DB]
            self.collection = self.database[COL]
            
            # If the info is authenticated then message will be printed
            if self.client.server_info():
                print("DBConnection is established with MongoDB!")

        # Otherwise, an error regarding the connection will be thrown
        except Exception as e:
            print(f"Connection failed! - {e}")

class CRUDObjects(object):
    def __init__(self, collection):

        """
        This try/catch will check for the documents collection in pymongo
        """
        try:
            if collection is None: # Collection is none raise error 
                raise ValueError("Collection is Empty!")
            
            # Will try to access document in the collection and count the documents
            collection.estimated_document_count()

            # for the documents in the collection assign to self.collection and print message
            self.collection = collection
            print("Database collection documents read!")

        except Exception as e:
            # Message will be display if the collection was not able to read or establish communication with DB
            print(f"Database document extraction failed! - {e}")
            self.collection = None

        """
        This function 'create' accept as a parameter the variable 'data'
        this function will add a new document to the 'animal' collection in db 'ACC'
        the function includes a condiction statement that check data validation
        if var data has some info them var result will get that information and will be insert
        into the collection inside the db
        then will return a trye if the document was saved

         else if var data is empty a message will be thrown e:
        """

    def create(self, data):
        if data is not None: 
            result = self.collection.insert_one(data)
            return result.inserted_id is not None
        else:
            raise Exception("Nothing to save, because data parameter is empty")
    
    """
    this function read documents from the collecion 'animals'
    Functin read has one params data assigned to None
    var data = data or {} why? if user provide a document informacion data will be assigned to the document value otherwise will look for all record in the collection
    var result will find into the collection base on the data info and the _id will be hided will no display when the document is read
    var data list is assigned to  the list(result) for a record or all record returned
    The FOR loop will look into the list of record and will separate each record for printing
    """
    def read(self, data=None):     
        data = data or {}
        results = self.collection.find(data, {"_id": False})
        data_list = list(results)

        for document in data_list:
            print(document)
        return data_list

    
    """
    This is the update function that will be call whenever a record needs to be updated.
    This function recieve two params, search and newData
    Search contains the information for the existing document within the db then newData will update the existing data
    If both params are not None then var result will look into the db and will replace the record then return result and how many record were updated
    Otherwise will thrown and error
    """
    def update(self, search, newData):
        if search and newData is not None:
            result = self.collection.update_many(search, {"$set": newData})
            return result.modified_count
        else:
            raise Exception("Existing document or New data record are empty!")
    
    """
    This function will delete a record requested by the user
    The function accept one param deleteData
    If deleteData is not Empty then reult will look into the collection and will get the record or records
    Then will print how many record were removed otherwise a exception will be thrown 
    """
    def delete(self, deleteData):
        if deleteData is not None:
            result = self.collection.delete_many(deleteData)
            print("Total document removed: ", result.deleted_count)
            return result.deleted_count
        else:
            raise Exception("Nothing to delete, because data parameter is empty")
        