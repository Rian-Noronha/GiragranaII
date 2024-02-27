package com.rn.giragrana.repository.room
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rn.giragrana.model.Client
import com.rn.giragrana.model.Product
import com.rn.giragrana.model.Resale
import com.rn.giragrana.repository.sqlite.DATABASE_NAME
import com.rn.giragrana.repository.sqlite.DATABASE_VERSION

@Database(entities = [Client::class, Product::class, Resale::class], version = DATABASE_VERSION)
abstract class GiragranaDatabase : RoomDatabase(){
    abstract fun clientDao(): ClientDao
    abstract fun productDao(): ProductDao
    abstract fun resaleDao(): ResaleDao

    companion object {
        private var instance:  GiragranaDatabase? = null
        fun getDatabase(context: Context): GiragranaDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    GiragranaDatabase::class.java,
                    DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as GiragranaDatabase
        }
        fun destroyInstance() {
            instance = null
        }
    }
}