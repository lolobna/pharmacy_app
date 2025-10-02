package com.example.pharmacieapp.DatabaseManagement

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.Settings
import android.util.Log
import com.example.pharmacieapp.classes.Commande
import com.example.pharmacieapp.classes.Medicament
import com.example.pharmacieapp.classes.Ordonnance
import com.example.pharmacieapp.classes.User
import java.sql.SQLException


object DataManager {

    // Insertion d'un utilisateur
    fun recupererMedicaments(dbHelper: DatabaseHelper): List<Medicament> {
        val medicaments = mutableListOf<Medicament>()
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_NOM,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_IMAGE,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_PRIX,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_TYPE,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_DESCRIPTION,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_COMPOSANTS,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_CONSEILS,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_CATEGORIE,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_QUANTITY
        )

        val cursor: Cursor = db.query(
            DatabaseContract.MedicamentEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val nom = getString(getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_NOM))
                val image = getString(getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_IMAGE))
                val prix = getDouble(getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_PRIX))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_TYPE))
                val description = getString(getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_DESCRIPTION))
                val composants = getString(getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_COMPOSANTS))
                val conseils = getString(getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_CONSEILS))
                val categorie = getString(getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_CATEGORIE))
                val quantity = getInt(getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_QUANTITY))

                val medicament = Medicament(id, nom, image, prix, type, description, composants, conseils, categorie, quantity)
                medicaments.add(medicament)
            }
        }
        cursor.close()
        return medicaments
    }

    fun recupererUtilisateurs(dbHelper: DatabaseHelper): List<User> {
        val utilisateurs = mutableListOf<User>()
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            DatabaseContract.UtilisateurEntry.COLUMN_NAME_NOM,
            DatabaseContract.UtilisateurEntry.COLUMN_NAME_PRENOM,
            DatabaseContract.UtilisateurEntry.COLUMN_NAME_EMAIL,
            DatabaseContract.UtilisateurEntry.COLUMN_NAME_PASSWORD,
            DatabaseContract.UtilisateurEntry.COLUMN_NAME_NUMERO,
            DatabaseContract.UtilisateurEntry.COLUMN_NAME_DATE_INSCRIPTION
        )

        val cursor: Cursor = db.query(
            DatabaseContract.UtilisateurEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val nom = getString(getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_NOM))
                val prenom = getString(getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_PRENOM))
                val email = getString(getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_EMAIL))
                val password = getString(getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_PASSWORD))
                val numero = getString(getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_NUMERO))
                val dateInscription = getString(getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_DATE_INSCRIPTION))

                val utilisateur = User(id, nom, prenom, email, password, numero, dateInscription)
                utilisateurs.add(utilisateur)
            }
        }
        cursor.close()
        return utilisateurs
    }



    fun insertMedicament(dbHelper: DatabaseHelper, medicament: Medicament) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_NOM, medicament.nom)
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_IMAGE, medicament.image)
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_PRIX, medicament.prix)
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_TYPE, medicament.type)
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_DESCRIPTION, medicament.description)
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_COMPOSANTS, medicament.composants)
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_CONSEILS, medicament.conseils)
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_CATEGORIE, medicament.categorie)
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_QUANTITY, medicament.quantity)
        }

        val newRowId = db.insert(DatabaseContract.MedicamentEntry.TABLE_NAME, null, values)
        if (newRowId == -1L) {
            Log.e("DataManager", "Erreur lors de l'insertion du médicament")
        } else {
            Log.d("DataManager", "Médicament inséré avec l'ID: $newRowId")
        }
    }
    fun insertUtilisateur(dbHelper: DatabaseHelper, utilisateur: User): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_NOM, utilisateur.nom)
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_PRENOM, utilisateur.prenom)
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_EMAIL, utilisateur.email)
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_PASSWORD, utilisateur.password)
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_NUMERO, utilisateur.numero)
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_DATE_INSCRIPTION, utilisateur.dateInscription)
        }

        val newRowId = db.insert(DatabaseContract.UtilisateurEntry.TABLE_NAME, null, values)
        if (newRowId == -1L) {
            Log.e("DataManager", "Erreur lors de l'insertion de l'utilisateur")
        } else {
            Log.d("DataManager", "Utilisateur inséré avec l'ID: $newRowId")
        }
        return newRowId
    }

    fun insererMedicamentsParDefaut(dbHelper: DatabaseHelper) {
        val medicaments = listOf(
            Medicament(
                id = 0,
                nom = "Paracétamol",
                image = "paracetamol",
                prix = 2.50,
                type = "Comprimé",
                description = "Soulage les douleurs légères à modérées et réduit la fièvre.",
                composants = "Paracétamol, Excipients, Eau purifiée",
                conseils = "À prendre avec de l'eau. Ne pas dépasser 4g par jour pour les adultes.",
                categorie = "Douleurs, Fièvre",
                quantity = 100
            ),
            Medicament(
                id = 0,
                nom = "Ibuprofène",
                image = "ibuprofene",
                prix = 3.00,
                type = "Comprimé",
                description = "Soulage les douleurs légères à modérées, réduit l'inflammation et la fièvre.",
                composants = "Ibuprofène, Excipients, Amidon de maïs, Stéarate de magnésium",
                conseils = "À prendre avec de l'eau. Ne pas dépasser la dose recommandée. À éviter à jeun pour prévenir les irritations gastriques.",
                categorie = "Douleurs, Fièvre",
                quantity = 150
            ),
            Medicament(
                id = 0,
                nom = "Simvastatine",
                image = "simvastatin",
                prix = 8.00,
                type = "Comprimé",
                description = "Réduit les niveaux de cholestérol.",
                composants = "Simvastatine, Excipients",
                conseils = "Prendre le soir.",
                categorie = "Cholestérol",
                quantity = 60
            ),
            Medicament(
                id = 0,
                nom = "Fenofibrate",
                image = "fenofibrate",
                prix = 9.00,
                type = "Comprimé",
                description = "Aide à réduire le cholestérol et les triglycérides.",
                composants = "Fenofibrate, Excipients",
                conseils = "Prendre avec les repas.",
                categorie = "Cholestérol",
                quantity = 70
            ),
            Medicament(
                id = 0,
                nom = "Levothyroxine",
                image = "levothyroxine",
                prix = 6.00,
                type = "Comprimé",
                description = "Utilisée pour traiter l'hypothyroïdie.",
                composants = "Levothyroxine, Excipients, Lactose",
                conseils = "À prendre à jeun, 30 minutes avant le petit-déjeuner.",
                categorie = "Hormones",
                quantity = 200
            ),
            Medicament(
                id = 0,
                nom = "Furosémide",
                image = "furosemide",
                prix = 3.50,
                type = "Comprimé",
                description = "Utilisé pour traiter l'hypertension et les oedèmes.",
                composants = "Furosémide, Excipients, Silice colloïdale",
                conseils = "Prendre le matin pour éviter les réveils nocturnes.",
                categorie = "Hypertension, Oedèmes",
                quantity = 120
            ),
            Medicament(
                id = 0,
                nom = "Atorvastatine",
                image = "atorvastatin",
                prix = 7.00,
                type = "Comprimé",
                description = "Réduit les niveaux de cholestérol LDL.",
                composants = "Atorvastatine, Excipients, Amidon prégélatinisé",
                conseils = "Prendre le soir pour une meilleure absorption.",
                categorie = "Cholestérol",
                quantity = 100
            ),
            Medicament(
                id = 0,
                nom = "Salbutamol",
                image = "salbutamol",
                prix = 5.50,
                type = "Inhalateur",
                description = "Utilisé pour traiter l'asthme et les troubles respiratoires.",
                composants = "Salbutamol, Excipients, HFA-134a",
                conseils = "Utiliser en inhalation selon les recommandations du médecin.",
                categorie = "Asthme, Respiration",
                quantity = 80
            ),
            Medicament(
                id = 0,
                nom = "Clopidogrel",
                image = "clopidogrel",
                prix = 8.00,
                type = "Comprimé",
                description = "Empêche la formation de caillots sanguins.",
                composants = "Clopidogrel, Excipients, Cellulose",
                conseils = "Prendre quotidiennement pour réduire le risque de crises cardiaques.",
                categorie = "Diabète",
                quantity = 100
            ),
            Medicament(
                id = 0,
                nom = "Ranitidine",
                image = "ranitidine",
                prix = 5.00,
                type = "Comprimé",
                description = "Utilisé pour traiter et prévenir les ulcères d'estomac.",
                composants = "Ranitidine, Excipients, Silice colloïdale",
                conseils = "Prendre avant le repas du soir.",
                categorie = "Ulcères",
                quantity = 90
            ),
            Medicament(
                id = 0,
                nom = "Amlodipine",
                image = "amlodipine",
                prix = 4.50,
                type = "Comprimé",
                description = "Utilisé pour traiter l'hypertension artérielle.",
                composants = "Amlodipine, Excipients, Cellulose microcristalline",
                conseils = "À prendre une fois par jour, avec ou sans nourriture.",
                categorie = "Hypertension, Oedèmes",
                quantity = 90
            ),
            Medicament(
                id = 0,
                nom = "Lisinopril",
                image = "lisinopril",
                prix = 3.00,
                type = "Comprimé",
                description = "Utilisé pour traiter l'hypertension artérielle et l'insuffisance cardiaque.",
                composants = "Lisinopril, Excipients, Amidon de maïs",
                conseils = "Prendre à la même heure chaque jour, de préférence le matin.",
                categorie = "Hypertension, Oedèmes",
                quantity = 100
            ),
            Medicament(
                id = 0,
                nom = "Glibenclamide",
                image = "glibenclamide",
                prix = 5.00,
                type = "Gel",
                description = "Utilisé pour traiter le diabète de type 2.",
                composants = "Glibenclamide, Excipients, Lactose",
                conseils = "Prendre avec le petit-déjeuner ou le premier repas principal de la journée.",
                categorie = "Diabète",
                quantity = 120
            ),
            Medicament(
                id = 0,
                nom = "Naproxène",
                image = "naproxene",
                prix = 4.00,
                type = "Poudres",
                description = "Utilisé pour soulager la douleur et l'inflammation.",
                composants = "Naproxène, Excipients, Silice colloïdale",
                conseils = "Prendre après les repas pour minimiser les effets secondaires gastro-intestinaux.",
                categorie = "Douleurs, Fièvre",
                quantity = 180
            ),
            Medicament(
                id = 0,
                nom = "Amlodipine",
                image = "amlodipine", // Vous pouvez mettre le nom de l'image ou l'URL ici
                prix = 4.50,
                type = "Capsules",
                description = "Utilisé pour traiter l'hypertension artérielle.",
                composants = "Amlodipine, Excipients, Cellulose microcristalline",
                conseils = "À prendre une fois par jour, avec ou sans nourriture.",
                categorie = "Hypertension, Oedèmes",
                quantity = 90
            ),
            Medicament(
                id = 0,
                nom = "Lisinopril",
                image = "lisinopril",
                prix = 3.00,
                type = "Comprimé",
                description = "Utilisé pour traiter l'hypertension artérielle et l'insuffisance cardiaque.",
                composants = "Lisinopril, Excipients, Amidon de maïs",
                conseils = "Prendre à la même heure chaque jour, de préférence le matin.",
                categorie = "Hypertension, Oedèmes",
                quantity = 100
            ),
            Medicament(
                id = 0,
                nom = "Glibenclamide",
                image = "glibenclamide",
                prix = 5.00,
                type = "Capsules",
                description = "Utilisé pour traiter le diabète de type 2.",
                composants = "Glibenclamide, Excipients, Lactose",
                conseils = "Prendre avec le petit-déjeuner ou le premier repas principal de la journée.",
                categorie = "Diabète",
                quantity = 120
            ),
            Medicament(
                id = 0,
                nom = "Naproxène",
                image = "naproxene",
                prix = 4.00,
                type = "Comprimé",
                description = "Utilisé pour soulager la douleur et l'inflammation.",
                composants = "Naproxène, Excipients, Silice colloïdale",
                conseils = "Prendre après les repas pour minimiser les effets secondaires gastro-intestinaux.",
                categorie = "Douleurs, Fièvre",
                quantity = 180
            ),
            Medicament(
                id = 0,
                nom = "Fluoxétine",
                image = "fluoxetine",
                prix = 6.50,
                type = "Comprimé",
                description = "Utilisé pour traiter la dépression, le trouble obsessionnel-compulsif et l'anxiété.",
                composants = "Fluoxétine, Excipients, Silice colloïdale",
                conseils = "Prendre le matin pour éviter les troubles du sommeil.",
                categorie = "Dépression, Anxiété",
                quantity = 60
            ),
            Medicament(
                id = 0,
                nom = "Céphalexine",
                image = "cephalexin",
                prix = 5.50,
                type = "Suppositoires",
                description = "Utilisé pour traiter diverses infections bactériennes.",
                composants = "Céphalexine, Excipients, Stéarate de magnésium",
                conseils = "Prendre à intervalles réguliers pour maintenir un niveau constant de médicament dans le corps.",
                categorie = "Inflammations, Allergies",
                quantity = 100
            ),
            Medicament(
                id = 0,
                nom = "Prednisolone",
                image = "prednisolone",
                prix = 7.00,
                type = "Suppositoires",
                description = "Utilisé pour traiter les inflammations, les allergies et certaines affections auto-immunes.",
                composants = "Prednisolone, Excipients, Lactose",
                conseils = "Prendre avec de la nourriture pour éviter les irritations gastriques.",
                categorie = "Inflammations, Allergies",
                quantity = 90
            ),
            Medicament(
                id = 0,
                nom = "Amiodarone",
                image = "amiodarone",
                prix = 9.00,
                type = "Comprimé",
                description = "Utilisé pour traiter les troubles du rythme cardiaque.",
                composants = "Amiodarone, Excipients, Povidone",
                conseils = "Prendre selon les directives du médecin, généralement avec de la nourriture.",
                categorie = "Hormones",
                quantity = 80
            )
        )


        for (medicament in medicaments) {
            try {
                insertMedicament(dbHelper, medicament)
            } catch (e: Exception) {
                Log.e("DataManager", "Erreur lors de l'insertion du médicament ${medicament.nom}: ${e.message}")
            }
        }
    }


    fun estTableMedicamentsVide(dbHelper: DatabaseHelper): Boolean {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT COUNT(*) FROM ${DatabaseContract.MedicamentEntry.TABLE_NAME}", null)

        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()

        return count == 0
    }

    fun supprimerTousLesMedicaments(dbHelper: DatabaseHelper): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            db.execSQL("DELETE FROM medicaments")  // Suppression de toutes les lignes dans la table medicament
            Log.d("DataManager", "Tous les médicaments ont été supprimés.")
            true
        } catch (e: SQLException) {
            Log.e("DataManager", "Erreur lors de la suppression des médicaments: ${e.message}")
            false  // Retourne false si une erreur se produit
        } finally {
            db.close()
        }
    }








    fun recupererIdMedicamentParNom(dbHelper: DatabaseHelper, nom: String): Long? {
        var medicamentId: Long? = null
        val db = dbHelper.readableDatabase

        // Requête pour récupérer l'ID du médicament par son nom
        val cursor: Cursor = db.query(
            DatabaseContract.MedicamentEntry.TABLE_NAME,
            arrayOf(BaseColumns._ID), // Sélectionner l'ID
            "${DatabaseContract.MedicamentEntry.COLUMN_NAME_NOM} = ?", // Clause WHERE
            arrayOf(nom), // Arguments pour la clause WHERE
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            medicamentId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
        }

        cursor.close()
        return medicamentId
    }


    fun recupererUtilisateurParId(dbHelper: DatabaseHelper, userId: Long): User? {
        val db = dbHelper.readableDatabase
        var utilisateur: User? = null

        val cursor: Cursor = db.query(
            DatabaseContract.UtilisateurEntry.TABLE_NAME,
            null, // Récupérer toutes les colonnes
            "${BaseColumns._ID} = ?", // Clause WHERE
            arrayOf(userId.toString()), // Arguments pour la clause WHERE
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val nom = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_NOM))
            val prenom = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_PRENOM))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_EMAIL))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_PASSWORD))
            val numero = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_NUMERO)) // Assurez-vous que ce champ existe dans la base
            val dateInscription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UtilisateurEntry.COLUMN_NAME_DATE_INSCRIPTION))

            utilisateur = User(userId, nom, prenom, email, password, numero, dateInscription)
        }

        cursor.close()
        return utilisateur
    }

    fun getUserIdByEmail(dbHelper: DatabaseHelper, email: String): Long? {
        var userId: Long? = null
        val db = dbHelper.readableDatabase

        // Requête pour récupérer l'ID de l'utilisateur par son email
        val cursor: Cursor = db.query(
            DatabaseContract.UtilisateurEntry.TABLE_NAME,
            arrayOf(BaseColumns._ID), // Sélectionnez uniquement l'ID
            "${DatabaseContract.UtilisateurEntry.COLUMN_NAME_EMAIL} = ?", // Clause WHERE
            arrayOf(email), // Arguments pour la clause WHERE
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
        }

        cursor.close()
        return userId
    }



    fun recupererIdsMedicaments(dbHelper: DatabaseHelper): List<Long> {
        val ids = mutableListOf<Long>()
        val db = dbHelper.readableDatabase


        val cursor: Cursor = db.query(
            DatabaseContract.MedicamentEntry.TABLE_NAME,
            arrayOf(BaseColumns._ID), // Sélectionnez uniquement l'ID
            null, // Pas de clause WHERE
            null, // Pas d'arguments pour la clause WHERE
            null, // Pas de GROUP BY
            null, // Pas de HAVING
            null  // Pas de ORDER BY
        )

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            ids.add(id)
        }
        cursor.close()
        return ids
    }


    // Récupérer tous les types de médicaments uniques
    @SuppressLint("Range")
    fun recupererTypesMedicaments(dbHelper: DatabaseHelper): List<String> {
        val types = mutableListOf<String>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT DISTINCT type FROM medicaments", null)

        if (cursor.moveToFirst()) {
            do {
                val type = cursor.getString(cursor.getColumnIndex("type"))
                types.add(type)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return types
    }

    // Récupérer toutes les catégories de médicaments uniques
    @SuppressLint("Range")
    fun recupererCategoriesMedicaments(dbHelper: DatabaseHelper): List<String> {
        val categories = mutableListOf<String>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT DISTINCT categorie FROM medicaments", null)

        if (cursor.moveToFirst()) {
            do {
                val categorie = cursor.getString(cursor.getColumnIndex("categorie"))
                categories.add(categorie)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return categories
    }

    fun insertUser(dbHelper: DatabaseHelper, user: User): Boolean {
        val db = dbHelper.writableDatabase

        // Requête pour vérifier si l'utilisateur existe déjà avec l'email
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseContract.UtilisateurEntry.TABLE_NAME} WHERE ${DatabaseContract.UtilisateurEntry.COLUMN_NAME_EMAIL} = ?",
            arrayOf(user.email)
        )

        // Vérifiez si l'utilisateur existe déjà
        val userExists = cursor.count > 0
        cursor.close()

        if (userExists) {
            return false // L'utilisateur existe déjà
        }

        // Insérer le nouvel utilisateur
        val values = ContentValues().apply {
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_NOM, user.nom)
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_PRENOM, user.prenom)
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_EMAIL, user.email)
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_PASSWORD, user.password)
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_NUMERO, user.numero) // Ajoutez le numéro si nécessaire
            put(DatabaseContract.UtilisateurEntry.COLUMN_NAME_DATE_INSCRIPTION, System.currentTimeMillis().toString()) // Date d'inscription actuelle
        }

        // Insérer dans la base de données
        val newRowId = db.insert(DatabaseContract.UtilisateurEntry.TABLE_NAME, null, values)

        return newRowId != -1L // Si l'insertion a échoué, la fonction retournera false
    }

    fun checkUser(dbHelper: DatabaseHelper, email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        // Requête pour vérifier si un utilisateur avec le même email et mot de passe existe
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseContract.UtilisateurEntry.TABLE_NAME} WHERE ${DatabaseContract.UtilisateurEntry.COLUMN_NAME_EMAIL} = ? AND ${DatabaseContract.UtilisateurEntry.COLUMN_NAME_PASSWORD} = ?",
            arrayOf(email, password)
        )

        // Vérifie si l'utilisateur existe
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }


    fun insertReservation(
        dbHelper: DatabaseHelper,
        medicamentId: Long,
        userId: Long,
        quantity: Int,
        dateReservation: String
    ) {
        // Obtenez une instance en écriture de la base de données
        val db = dbHelper.writableDatabase

        // Ajoutez des logs pour vérifier les données reçues
        Log.d("insertReservation", "MedicamentID: $medicamentId, UserID: $userId, Quantity: $quantity, DateReservation: $dateReservation")

        try {
            // Préparez les valeurs à insérer dans la table
            val values = ContentValues().apply {
                put(DatabaseContract.CommandeEntry.COLUMN_NAME_MEDICAMENT_ID, medicamentId)
                put(DatabaseContract.CommandeEntry.COLUMN_NAME_USER_ID, userId)
                put(DatabaseContract.CommandeEntry.COLUMN_NAME_QUANTITY_ACHETE, quantity)
                put(DatabaseContract.CommandeEntry.COLUMN_NAME_DATE_RESERVATION, dateReservation)
            }

            // Insérez les données dans la table des réservations
            val result = db.insert(DatabaseContract.CommandeEntry.TABLE_NAME, null, values)

            if (result == -1L) {
                Log.e("insertReservation", "Échec de l'insertion de la réservation.")
            } else {
                Log.d("insertReservation", "Réservation insérée avec succès, ID: $result")
            }
        } catch (e: Exception) {
            Log.e("insertReservation", "Erreur lors de l'insertion: ${e.message}")
        } finally {
            db.close() // Fermez la base de données dans tous les cas
        }
    }


        fun getUserOrders(dbHelper: DatabaseHelper, userId: Long): List<Commande> {
            val orders = mutableListOf<Commande>()
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.CommandeEntry.TABLE_NAME} WHERE ${DatabaseContract.CommandeEntry.COLUMN_NAME_USER_ID} = ?", arrayOf(userId.toString()))

            if (cursor.moveToFirst()) {
                do {
                    val order = Commande(
                        idUser = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.CommandeEntry.COLUMN_NAME_USER_ID)),
                        idMedicament = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.CommandeEntry.COLUMN_NAME_MEDICAMENT_ID)),
                        quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CommandeEntry.COLUMN_NAME_QUANTITY_ACHETE)),
                        DateReservation = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CommandeEntry.COLUMN_NAME_DATE_RESERVATION))
                    )
                    orders.add(order)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return orders
        }

    fun recupererMedicamentParId(dbHelper: DatabaseHelper, medicamentId: Long): Medicament? {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_NOM,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_IMAGE,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_PRIX,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_TYPE,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_DESCRIPTION,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_COMPOSANTS,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_CONSEILS,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_CATEGORIE,
            DatabaseContract.MedicamentEntry.COLUMN_NAME_QUANTITY
        )

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(medicamentId.toString())

        val cursor: Cursor = db.query(
            DatabaseContract.MedicamentEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var medicament: Medicament? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val nom = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_NOM))
            val image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_IMAGE))
            val prix = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_PRIX))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_TYPE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_DESCRIPTION))
            val composants = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_COMPOSANTS))
            val conseils = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_CONSEILS))
            val categorie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_CATEGORIE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_QUANTITY))

            medicament = Medicament(id, nom, image, prix, type, description, composants, conseils, categorie, quantity)
        }
        cursor.close()
        return medicament
    }


    fun saveOrdonnance(dbHelper: DatabaseHelper, ordonnance: Ordonnance): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.OrdonnanceEntry.COLUMN_NAME_USER_ID, ordonnance.userId)
            put(DatabaseContract.OrdonnanceEntry.COLUMN_NAME_ORDONNANCE_IMAGE, ordonnance.ordonnanceImage)
            put(DatabaseContract.OrdonnanceEntry.COLUMN_NAME_DATE_ORDONNANCE, ordonnance.dateOrdonnance)
        }

        val newRowId = db.insert(DatabaseContract.OrdonnanceEntry.TABLE_NAME, null, values)
        if (newRowId == -1L) {
            Log.e("DataManager", "Erreur lors de l'enregistrement de l'ordonnance")
        } else {
            Log.d("DataManager", "Ordonnance enregistrée avec l'ID: $newRowId")
        }
        return newRowId
    }


    fun getUserOrdonnances(dbHelper: DatabaseHelper, userId: Long): List<Ordonnance> {
        val ordonnances = mutableListOf<Ordonnance>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.OrdonnanceEntry.TABLE_NAME,
            null,
            "${DatabaseContract.OrdonnanceEntry.COLUMN_NAME_USER_ID} = ?",
            arrayOf(userId.toString()),
            null,
            null,
            "${BaseColumns._ID} DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val user_id = getLong(getColumnIndexOrThrow(DatabaseContract.OrdonnanceEntry.COLUMN_NAME_USER_ID))
                val ordonnanceImage = getString(getColumnIndexOrThrow(DatabaseContract.OrdonnanceEntry.COLUMN_NAME_ORDONNANCE_IMAGE))
                val dateOrdonnance = getString(getColumnIndexOrThrow(DatabaseContract.OrdonnanceEntry.COLUMN_NAME_DATE_ORDONNANCE))

                val ordonnance = Ordonnance(id, user_id, ordonnanceImage, dateOrdonnance)
                ordonnances.add(ordonnance)
            }
        }
        cursor.close()
        return ordonnances
    }

    // Récupérer la quantité actuelle d'un médicament par son ID
    fun getCurrentQuantity(dbHelper: DatabaseHelper, medicamentId: Long): Int {
        val db = dbHelper.readableDatabase
        var currentQuantity = 0

        val cursor: Cursor = db.query(
            DatabaseContract.MedicamentEntry.TABLE_NAME,
            arrayOf(DatabaseContract.MedicamentEntry.COLUMN_NAME_QUANTITY), // Sélectionner uniquement la quantité
            "${BaseColumns._ID} = ?", // Clause WHERE
            arrayOf(medicamentId.toString()), // Arguments pour la clause WHERE
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_QUANTITY))
        }
        cursor.close()
        return currentQuantity
    }

    // Mettre à jour la quantité d'un médicament
    fun updateMedicamentQuantity(dbHelper: DatabaseHelper, medicamentId: Long, newQuantity: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.MedicamentEntry.COLUMN_NAME_QUANTITY, newQuantity) // Nouvelle quantité
        }

        val rowsAffected = db.update(
            DatabaseContract.MedicamentEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?", // Clause WHERE
            arrayOf(medicamentId.toString()) // Arguments pour la clause WHERE
        )

        if (rowsAffected > 0) {
            Log.d("DataManager", "Quantité mise à jour pour le médicament ID: $medicamentId")
        } else {
            Log.e("DataManager", "Échec de la mise à jour de la quantité pour le médicament ID: $medicamentId")
        }
    }
    fun recupererComposantsParId(dbHelper: DatabaseHelper, medicamentId: Long): List<String> {
        val composantsList = mutableListOf<String>()
        val db = dbHelper.readableDatabase // Obtenir une instance de la base de données

        // Effectuer la requête pour sélectionner les composants du médicament par son ID
        val cursor = db.query(
            DatabaseContract.MedicamentEntry.TABLE_NAME,
            arrayOf(DatabaseContract.MedicamentEntry.COLUMN_NAME_COMPOSANTS),
            "${BaseColumns._ID} = ?", // Clause WHERE
            arrayOf(medicamentId.toString()), // Arguments pour la clause WHERE
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            // Récupérer la chaîne de composants
            val composantsString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MedicamentEntry.COLUMN_NAME_COMPOSANTS))
            // Ajouter les composants à la liste après les avoir séparés par des virgules
            composantsList.addAll(composantsString.split(",").map { it.trim() })
        }
        cursor.close() // Fermer le curseur
        return composantsList // Retourner la liste des composants
    }

}
