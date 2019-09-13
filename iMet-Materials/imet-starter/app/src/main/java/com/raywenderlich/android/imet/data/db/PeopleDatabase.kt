package com.raywenderlich.android.imet.data.db

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.os.AsyncTask
import com.raywenderlich.android.imet.data.model.People
import com.raywenderlich.android.imet.data.net.PeopleInfoProvider


// 1
@Database(entities = [People::class], version = 1)
abstract class PeopleDatabase : RoomDatabase() {

    abstract fun peopleDao(): PeopleDao

    //2
    companion object {
        private val lock = Any()
        private const val DB_NAME = "People.db"
        private var INSTANCE: PeopleDatabase? = null

        fun prePopulate(database: PeopleDatabase, peopleList: List<People>) {
            for (people in peopleList) {
                AsyncTask.execute { database.peopleDao().insert(people) }
            }
        }
        //3
        fun getInstance(application: Application): PeopleDatabase {
            synchronized(PeopleDatabase.lock) {
                if (PeopleDatabase.INSTANCE == null) {
                    PeopleDatabase.INSTANCE =
                            Room.databaseBuilder(application, PeopleDatabase::class.java,
                                    PeopleDatabase.DB_NAME)
                                    .allowMainThreadQueries()
                                    .addCallback(object : RoomDatabase.Callback() {
                                        override fun onCreate(db: SupportSQLiteDatabase) {
                                            super.onCreate(db)
                                            PeopleDatabase.INSTANCE?.let {
                                                PeopleDatabase.prePopulate(it, PeopleInfoProvider.peopleList)

                                            }
                                        }

                                    })

                                    .build()
                }
                return PeopleDatabase.INSTANCE!!

            }
        }

    }



}
