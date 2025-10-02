package com.example.pharmacieapp.DatabaseManagement



import android.annotation.SuppressLint
import android.content.ContentValues
import android.provider.BaseColumns
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.room.util.getColumnIndexOrThrow
import com.example.pharmacieapp.classes.User


object DatabaseContract {
    object UtilisateurEntry : BaseColumns {
        const val TABLE_NAME = "utilisateurs"
        const val COLUMN_NAME_NOM = "nom"
        const val COLUMN_NAME_PRENOM = "prenom"
        const val COLUMN_NAME_EMAIL = "email"
        const val COLUMN_NAME_PASSWORD = "password"
        const val COLUMN_NAME_NUMERO = "numero"
        const val COLUMN_NAME_DATE_INSCRIPTION = "dateinsc"
    }

    object MedicamentEntry : BaseColumns {
        const val TABLE_NAME = "medicaments"
        const val COLUMN_NAME_NOM = "nom"
        const val COLUMN_NAME_IMAGE = "image"
        const val COLUMN_NAME_PRIX = "prix"
        const val COLUMN_NAME_TYPE = "type"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_COMPOSANTS = "composants"
        const val COLUMN_NAME_CONSEILS = "conseils"
        const val COLUMN_NAME_CATEGORIE = "categorie"
        const val COLUMN_NAME_QUANTITY = "quantity"
    }



    object CommandeEntry : BaseColumns {
        const val TABLE_NAME = "commandes"
        const val COLUMN_NAME_USER_ID = "iduser"
        const val COLUMN_NAME_MEDICAMENT_ID = "idmedicament"
        const val COLUMN_NAME_DATE_RESERVATION = "dateReservation"
        const val COLUMN_NAME_QUANTITY_ACHETE = "quantity"
    }


    object OrdonnanceEntry : BaseColumns {
        const val TABLE_NAME = "ordonnances"
        const val COLUMN_NAME_USER_ID = "iduser"
        const val COLUMN_NAME_ORDONNANCE_IMAGE = "ordonnaceImage"
        const val COLUMN_NAME_DATE_ORDONNANCE = "dateOrdonnance" // Date de l'ordonnance

    }

}

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    private val SQL_CREATE_UTILISATEURS =
        """
        CREATE TABLE ${DatabaseContract.UtilisateurEntry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${DatabaseContract.UtilisateurEntry.COLUMN_NAME_NOM} TEXT NOT NULL,
            ${DatabaseContract.UtilisateurEntry.COLUMN_NAME_PRENOM} TEXT NOT NULL,
            ${DatabaseContract.UtilisateurEntry.COLUMN_NAME_EMAIL} TEXT UNIQUE NOT NULL,
            ${DatabaseContract.UtilisateurEntry.COLUMN_NAME_PASSWORD} TEXT NOT NULL,
            ${DatabaseContract.UtilisateurEntry.COLUMN_NAME_NUMERO} TEXT NOT NULL,
            ${DatabaseContract.UtilisateurEntry.COLUMN_NAME_DATE_INSCRIPTION} TEXT NOT NULL
        )
        """.trimIndent()

    private val SQL_CREATE_MEDICAMENTS =
        """
        CREATE TABLE ${DatabaseContract.MedicamentEntry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${DatabaseContract.MedicamentEntry.COLUMN_NAME_NOM} TEXT NOT NULL,
            ${DatabaseContract.MedicamentEntry.COLUMN_NAME_IMAGE} TEXT,
            ${DatabaseContract.MedicamentEntry.COLUMN_NAME_PRIX} REAL NOT NULL,
            ${DatabaseContract.MedicamentEntry.COLUMN_NAME_TYPE} TEXT NOT NULL,
            ${DatabaseContract.MedicamentEntry.COLUMN_NAME_DESCRIPTION} TEXT,
            ${DatabaseContract.MedicamentEntry.COLUMN_NAME_COMPOSANTS} TEXT,
            ${DatabaseContract.MedicamentEntry.COLUMN_NAME_CONSEILS} TEXT,
            ${DatabaseContract.MedicamentEntry.COLUMN_NAME_CATEGORIE} TEXT NOT NULL,
            ${DatabaseContract.MedicamentEntry.COLUMN_NAME_QUANTITY} INTEGER NOT NULL
        )
        """.trimIndent()

    private val SQL_CREATE_COMMANDES =
        """
        CREATE TABLE ${DatabaseContract.CommandeEntry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${DatabaseContract.CommandeEntry.COLUMN_NAME_USER_ID} INTEGER NOT NULL,
            ${DatabaseContract.CommandeEntry.COLUMN_NAME_MEDICAMENT_ID} INTEGER NOT NULL,
            ${DatabaseContract.CommandeEntry.COLUMN_NAME_DATE_RESERVATION} TEXT NOT NULL,
            ${DatabaseContract.CommandeEntry.COLUMN_NAME_QUANTITY_ACHETE} INTEGER NOT NULL,
            FOREIGN KEY(${DatabaseContract.CommandeEntry.COLUMN_NAME_USER_ID}) 
                REFERENCES ${DatabaseContract.UtilisateurEntry.TABLE_NAME}(${BaseColumns._ID}),
            FOREIGN KEY(${DatabaseContract.CommandeEntry.COLUMN_NAME_MEDICAMENT_ID}) 
                REFERENCES ${DatabaseContract.MedicamentEntry.TABLE_NAME}(${BaseColumns._ID})
        )
        """.trimIndent()

    private val SQL_CREATE_ORDONNANCES =
        """
        CREATE TABLE ${DatabaseContract.OrdonnanceEntry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${DatabaseContract.OrdonnanceEntry.COLUMN_NAME_USER_ID} INTEGER NOT NULL,
            ${DatabaseContract.OrdonnanceEntry.COLUMN_NAME_ORDONNANCE_IMAGE} TEXT NOT NULL,
            ${DatabaseContract.OrdonnanceEntry.COLUMN_NAME_DATE_ORDONNANCE} TEXT NOT NULL,
            FOREIGN KEY(${DatabaseContract.OrdonnanceEntry.COLUMN_NAME_USER_ID}) 
                REFERENCES ${DatabaseContract.UtilisateurEntry.TABLE_NAME}(${BaseColumns._ID})
        )
        """.trimIndent()

    private val SQL_DELETE_ENTRIES =
        buildString {
            append("DROP TABLE IF EXISTS ${DatabaseContract.UtilisateurEntry.TABLE_NAME};")
            append("DROP TABLE IF EXISTS ${DatabaseContract.MedicamentEntry.TABLE_NAME};")
            append("DROP TABLE IF EXISTS ${DatabaseContract.CommandeEntry.TABLE_NAME};")
            append("DROP TABLE IF EXISTS ${DatabaseContract.OrdonnanceEntry.TABLE_NAME};")
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_UTILISATEURS)
        db.execSQL(SQL_CREATE_MEDICAMENTS)
        db.execSQL(SQL_CREATE_COMMANDES)
        db.execSQL(SQL_CREATE_ORDONNANCES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("DatabaseHelper", "Mise à jour de la base de données de $oldVersion à $newVersion")
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "medica.db"
    }
}




