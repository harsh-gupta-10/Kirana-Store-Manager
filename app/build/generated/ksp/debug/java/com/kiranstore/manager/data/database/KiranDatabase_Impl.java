package com.kiranstore.manager.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.kiranstore.manager.data.database.dao.BuyListDao;
import com.kiranstore.manager.data.database.dao.BuyListDao_Impl;
import com.kiranstore.manager.data.database.dao.CustomerDao;
import com.kiranstore.manager.data.database.dao.CustomerDao_Impl;
import com.kiranstore.manager.data.database.dao.DebtDao;
import com.kiranstore.manager.data.database.dao.DebtDao_Impl;
import com.kiranstore.manager.data.database.dao.RentalDao;
import com.kiranstore.manager.data.database.dao.RentalDao_Impl;
import com.kiranstore.manager.data.database.dao.ShopDao;
import com.kiranstore.manager.data.database.dao.ShopDao_Impl;
import com.kiranstore.manager.data.database.dao.TaskDao;
import com.kiranstore.manager.data.database.dao.TaskDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class KiranDatabase_Impl extends KiranDatabase {
  private volatile ShopDao _shopDao;

  private volatile CustomerDao _customerDao;

  private volatile DebtDao _debtDao;

  private volatile RentalDao _rentalDao;

  private volatile BuyListDao _buyListDao;

  private volatile TaskDao _taskDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `shops` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `shopName` TEXT NOT NULL, `ownerName` TEXT NOT NULL, `phone` TEXT NOT NULL, `address` TEXT NOT NULL, `logoUri` TEXT, `userId` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `customers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `shopId` INTEGER NOT NULL, `name` TEXT NOT NULL, `phone` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`shopId`) REFERENCES `shops`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_customers_shopId` ON `customers` (`shopId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `debts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `customerId` INTEGER NOT NULL, `shopId` INTEGER NOT NULL, `itemName` TEXT NOT NULL, `amount` REAL NOT NULL, `date` INTEGER NOT NULL, `notes` TEXT NOT NULL, `status` TEXT NOT NULL, FOREIGN KEY(`customerId`) REFERENCES `customers`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`shopId`) REFERENCES `shops`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_debts_customerId` ON `debts` (`customerId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_debts_shopId` ON `debts` (`shopId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `debt_payments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `customerId` INTEGER NOT NULL, `shopId` INTEGER NOT NULL, `amount` REAL NOT NULL, `date` INTEGER NOT NULL, `notes` TEXT NOT NULL, FOREIGN KEY(`customerId`) REFERENCES `customers`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`shopId`) REFERENCES `shops`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_debt_payments_customerId` ON `debt_payments` (`customerId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_debt_payments_shopId` ON `debt_payments` (`shopId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rentals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `customerId` INTEGER NOT NULL, `shopId` INTEGER NOT NULL, `machineName` TEXT NOT NULL, `deposit` REAL NOT NULL, `rentAmount` REAL NOT NULL, `startDate` INTEGER NOT NULL, `returnDate` INTEGER, `status` TEXT NOT NULL, FOREIGN KEY(`customerId`) REFERENCES `customers`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`shopId`) REFERENCES `shops`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rentals_customerId` ON `rentals` (`customerId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rentals_shopId` ON `rentals` (`shopId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rental_machines` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `shopId` INTEGER NOT NULL, `machineName` TEXT NOT NULL, `rentPrice` REAL NOT NULL, `deposit` REAL NOT NULL, FOREIGN KEY(`shopId`) REFERENCES `shops`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rental_machines_shopId` ON `rental_machines` (`shopId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `buy_list_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `shopId` INTEGER NOT NULL, `name` TEXT NOT NULL, `quantity` TEXT NOT NULL, `priority` TEXT NOT NULL, `notes` TEXT NOT NULL, `isPurchased` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`shopId`) REFERENCES `shops`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_buy_list_items_shopId` ON `buy_list_items` (`shopId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `shopId` INTEGER NOT NULL, `name` TEXT NOT NULL, `date` INTEGER NOT NULL, `notes` TEXT NOT NULL, `status` TEXT NOT NULL, FOREIGN KEY(`shopId`) REFERENCES `shops`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_shopId` ON `tasks` (`shopId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5b86facb2bc1dc24e780e69fa90f61b6')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `shops`");
        db.execSQL("DROP TABLE IF EXISTS `customers`");
        db.execSQL("DROP TABLE IF EXISTS `debts`");
        db.execSQL("DROP TABLE IF EXISTS `debt_payments`");
        db.execSQL("DROP TABLE IF EXISTS `rentals`");
        db.execSQL("DROP TABLE IF EXISTS `rental_machines`");
        db.execSQL("DROP TABLE IF EXISTS `buy_list_items`");
        db.execSQL("DROP TABLE IF EXISTS `tasks`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsShops = new HashMap<String, TableInfo.Column>(8);
        _columnsShops.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("shopName", new TableInfo.Column("shopName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("ownerName", new TableInfo.Column("ownerName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("address", new TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("logoUri", new TableInfo.Column("logoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShops.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysShops = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesShops = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoShops = new TableInfo("shops", _columnsShops, _foreignKeysShops, _indicesShops);
        final TableInfo _existingShops = TableInfo.read(db, "shops");
        if (!_infoShops.equals(_existingShops)) {
          return new RoomOpenHelper.ValidationResult(false, "shops(com.kiranstore.manager.data.database.entities.ShopEntity).\n"
                  + " Expected:\n" + _infoShops + "\n"
                  + " Found:\n" + _existingShops);
        }
        final HashMap<String, TableInfo.Column> _columnsCustomers = new HashMap<String, TableInfo.Column>(5);
        _columnsCustomers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("shopId", new TableInfo.Column("shopId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCustomers = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCustomers.add(new TableInfo.ForeignKey("shops", "CASCADE", "NO ACTION", Arrays.asList("shopId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCustomers = new HashSet<TableInfo.Index>(1);
        _indicesCustomers.add(new TableInfo.Index("index_customers_shopId", false, Arrays.asList("shopId"), Arrays.asList("ASC")));
        final TableInfo _infoCustomers = new TableInfo("customers", _columnsCustomers, _foreignKeysCustomers, _indicesCustomers);
        final TableInfo _existingCustomers = TableInfo.read(db, "customers");
        if (!_infoCustomers.equals(_existingCustomers)) {
          return new RoomOpenHelper.ValidationResult(false, "customers(com.kiranstore.manager.data.database.entities.CustomerEntity).\n"
                  + " Expected:\n" + _infoCustomers + "\n"
                  + " Found:\n" + _existingCustomers);
        }
        final HashMap<String, TableInfo.Column> _columnsDebts = new HashMap<String, TableInfo.Column>(8);
        _columnsDebts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("customerId", new TableInfo.Column("customerId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("shopId", new TableInfo.Column("shopId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("itemName", new TableInfo.Column("itemName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebts.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDebts = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysDebts.add(new TableInfo.ForeignKey("customers", "CASCADE", "NO ACTION", Arrays.asList("customerId"), Arrays.asList("id")));
        _foreignKeysDebts.add(new TableInfo.ForeignKey("shops", "CASCADE", "NO ACTION", Arrays.asList("shopId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDebts = new HashSet<TableInfo.Index>(2);
        _indicesDebts.add(new TableInfo.Index("index_debts_customerId", false, Arrays.asList("customerId"), Arrays.asList("ASC")));
        _indicesDebts.add(new TableInfo.Index("index_debts_shopId", false, Arrays.asList("shopId"), Arrays.asList("ASC")));
        final TableInfo _infoDebts = new TableInfo("debts", _columnsDebts, _foreignKeysDebts, _indicesDebts);
        final TableInfo _existingDebts = TableInfo.read(db, "debts");
        if (!_infoDebts.equals(_existingDebts)) {
          return new RoomOpenHelper.ValidationResult(false, "debts(com.kiranstore.manager.data.database.entities.DebtEntity).\n"
                  + " Expected:\n" + _infoDebts + "\n"
                  + " Found:\n" + _existingDebts);
        }
        final HashMap<String, TableInfo.Column> _columnsDebtPayments = new HashMap<String, TableInfo.Column>(6);
        _columnsDebtPayments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("customerId", new TableInfo.Column("customerId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("shopId", new TableInfo.Column("shopId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDebtPayments.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDebtPayments = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysDebtPayments.add(new TableInfo.ForeignKey("customers", "CASCADE", "NO ACTION", Arrays.asList("customerId"), Arrays.asList("id")));
        _foreignKeysDebtPayments.add(new TableInfo.ForeignKey("shops", "CASCADE", "NO ACTION", Arrays.asList("shopId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDebtPayments = new HashSet<TableInfo.Index>(2);
        _indicesDebtPayments.add(new TableInfo.Index("index_debt_payments_customerId", false, Arrays.asList("customerId"), Arrays.asList("ASC")));
        _indicesDebtPayments.add(new TableInfo.Index("index_debt_payments_shopId", false, Arrays.asList("shopId"), Arrays.asList("ASC")));
        final TableInfo _infoDebtPayments = new TableInfo("debt_payments", _columnsDebtPayments, _foreignKeysDebtPayments, _indicesDebtPayments);
        final TableInfo _existingDebtPayments = TableInfo.read(db, "debt_payments");
        if (!_infoDebtPayments.equals(_existingDebtPayments)) {
          return new RoomOpenHelper.ValidationResult(false, "debt_payments(com.kiranstore.manager.data.database.entities.DebtPaymentEntity).\n"
                  + " Expected:\n" + _infoDebtPayments + "\n"
                  + " Found:\n" + _existingDebtPayments);
        }
        final HashMap<String, TableInfo.Column> _columnsRentals = new HashMap<String, TableInfo.Column>(9);
        _columnsRentals.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("customerId", new TableInfo.Column("customerId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("shopId", new TableInfo.Column("shopId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("machineName", new TableInfo.Column("machineName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("deposit", new TableInfo.Column("deposit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("rentAmount", new TableInfo.Column("rentAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("returnDate", new TableInfo.Column("returnDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentals.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRentals = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysRentals.add(new TableInfo.ForeignKey("customers", "CASCADE", "NO ACTION", Arrays.asList("customerId"), Arrays.asList("id")));
        _foreignKeysRentals.add(new TableInfo.ForeignKey("shops", "CASCADE", "NO ACTION", Arrays.asList("shopId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRentals = new HashSet<TableInfo.Index>(2);
        _indicesRentals.add(new TableInfo.Index("index_rentals_customerId", false, Arrays.asList("customerId"), Arrays.asList("ASC")));
        _indicesRentals.add(new TableInfo.Index("index_rentals_shopId", false, Arrays.asList("shopId"), Arrays.asList("ASC")));
        final TableInfo _infoRentals = new TableInfo("rentals", _columnsRentals, _foreignKeysRentals, _indicesRentals);
        final TableInfo _existingRentals = TableInfo.read(db, "rentals");
        if (!_infoRentals.equals(_existingRentals)) {
          return new RoomOpenHelper.ValidationResult(false, "rentals(com.kiranstore.manager.data.database.entities.RentalEntity).\n"
                  + " Expected:\n" + _infoRentals + "\n"
                  + " Found:\n" + _existingRentals);
        }
        final HashMap<String, TableInfo.Column> _columnsRentalMachines = new HashMap<String, TableInfo.Column>(5);
        _columnsRentalMachines.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalMachines.put("shopId", new TableInfo.Column("shopId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalMachines.put("machineName", new TableInfo.Column("machineName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalMachines.put("rentPrice", new TableInfo.Column("rentPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRentalMachines.put("deposit", new TableInfo.Column("deposit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRentalMachines = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRentalMachines.add(new TableInfo.ForeignKey("shops", "CASCADE", "NO ACTION", Arrays.asList("shopId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRentalMachines = new HashSet<TableInfo.Index>(1);
        _indicesRentalMachines.add(new TableInfo.Index("index_rental_machines_shopId", false, Arrays.asList("shopId"), Arrays.asList("ASC")));
        final TableInfo _infoRentalMachines = new TableInfo("rental_machines", _columnsRentalMachines, _foreignKeysRentalMachines, _indicesRentalMachines);
        final TableInfo _existingRentalMachines = TableInfo.read(db, "rental_machines");
        if (!_infoRentalMachines.equals(_existingRentalMachines)) {
          return new RoomOpenHelper.ValidationResult(false, "rental_machines(com.kiranstore.manager.data.database.entities.RentalMachineEntity).\n"
                  + " Expected:\n" + _infoRentalMachines + "\n"
                  + " Found:\n" + _existingRentalMachines);
        }
        final HashMap<String, TableInfo.Column> _columnsBuyListItems = new HashMap<String, TableInfo.Column>(8);
        _columnsBuyListItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuyListItems.put("shopId", new TableInfo.Column("shopId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuyListItems.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuyListItems.put("quantity", new TableInfo.Column("quantity", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuyListItems.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuyListItems.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuyListItems.put("isPurchased", new TableInfo.Column("isPurchased", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBuyListItems.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBuyListItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBuyListItems.add(new TableInfo.ForeignKey("shops", "CASCADE", "NO ACTION", Arrays.asList("shopId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBuyListItems = new HashSet<TableInfo.Index>(1);
        _indicesBuyListItems.add(new TableInfo.Index("index_buy_list_items_shopId", false, Arrays.asList("shopId"), Arrays.asList("ASC")));
        final TableInfo _infoBuyListItems = new TableInfo("buy_list_items", _columnsBuyListItems, _foreignKeysBuyListItems, _indicesBuyListItems);
        final TableInfo _existingBuyListItems = TableInfo.read(db, "buy_list_items");
        if (!_infoBuyListItems.equals(_existingBuyListItems)) {
          return new RoomOpenHelper.ValidationResult(false, "buy_list_items(com.kiranstore.manager.data.database.entities.BuyListItemEntity).\n"
                  + " Expected:\n" + _infoBuyListItems + "\n"
                  + " Found:\n" + _existingBuyListItems);
        }
        final HashMap<String, TableInfo.Column> _columnsTasks = new HashMap<String, TableInfo.Column>(6);
        _columnsTasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("shopId", new TableInfo.Column("shopId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTasks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTasks.add(new TableInfo.ForeignKey("shops", "CASCADE", "NO ACTION", Arrays.asList("shopId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTasks = new HashSet<TableInfo.Index>(1);
        _indicesTasks.add(new TableInfo.Index("index_tasks_shopId", false, Arrays.asList("shopId"), Arrays.asList("ASC")));
        final TableInfo _infoTasks = new TableInfo("tasks", _columnsTasks, _foreignKeysTasks, _indicesTasks);
        final TableInfo _existingTasks = TableInfo.read(db, "tasks");
        if (!_infoTasks.equals(_existingTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "tasks(com.kiranstore.manager.data.database.entities.TaskEntity).\n"
                  + " Expected:\n" + _infoTasks + "\n"
                  + " Found:\n" + _existingTasks);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "5b86facb2bc1dc24e780e69fa90f61b6", "8dd81fb9c08981be348a38e9c1bc6774");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "shops","customers","debts","debt_payments","rentals","rental_machines","buy_list_items","tasks");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `shops`");
      _db.execSQL("DELETE FROM `customers`");
      _db.execSQL("DELETE FROM `debts`");
      _db.execSQL("DELETE FROM `debt_payments`");
      _db.execSQL("DELETE FROM `rentals`");
      _db.execSQL("DELETE FROM `rental_machines`");
      _db.execSQL("DELETE FROM `buy_list_items`");
      _db.execSQL("DELETE FROM `tasks`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ShopDao.class, ShopDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CustomerDao.class, CustomerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DebtDao.class, DebtDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RentalDao.class, RentalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BuyListDao.class, BuyListDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ShopDao shopDao() {
    if (_shopDao != null) {
      return _shopDao;
    } else {
      synchronized(this) {
        if(_shopDao == null) {
          _shopDao = new ShopDao_Impl(this);
        }
        return _shopDao;
      }
    }
  }

  @Override
  public CustomerDao customerDao() {
    if (_customerDao != null) {
      return _customerDao;
    } else {
      synchronized(this) {
        if(_customerDao == null) {
          _customerDao = new CustomerDao_Impl(this);
        }
        return _customerDao;
      }
    }
  }

  @Override
  public DebtDao debtDao() {
    if (_debtDao != null) {
      return _debtDao;
    } else {
      synchronized(this) {
        if(_debtDao == null) {
          _debtDao = new DebtDao_Impl(this);
        }
        return _debtDao;
      }
    }
  }

  @Override
  public RentalDao rentalDao() {
    if (_rentalDao != null) {
      return _rentalDao;
    } else {
      synchronized(this) {
        if(_rentalDao == null) {
          _rentalDao = new RentalDao_Impl(this);
        }
        return _rentalDao;
      }
    }
  }

  @Override
  public BuyListDao buyListDao() {
    if (_buyListDao != null) {
      return _buyListDao;
    } else {
      synchronized(this) {
        if(_buyListDao == null) {
          _buyListDao = new BuyListDao_Impl(this);
        }
        return _buyListDao;
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }
}
