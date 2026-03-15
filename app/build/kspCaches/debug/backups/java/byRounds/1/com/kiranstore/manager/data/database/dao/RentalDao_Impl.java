package com.kiranstore.manager.data.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.kiranstore.manager.data.database.entities.RentalEntity;
import com.kiranstore.manager.data.database.entities.RentalMachineEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RentalDao_Impl implements RentalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RentalMachineEntity> __insertionAdapterOfRentalMachineEntity;

  private final EntityInsertionAdapter<RentalEntity> __insertionAdapterOfRentalEntity;

  private final EntityDeletionOrUpdateAdapter<RentalMachineEntity> __deletionAdapterOfRentalMachineEntity;

  private final EntityDeletionOrUpdateAdapter<RentalEntity> __deletionAdapterOfRentalEntity;

  private final EntityDeletionOrUpdateAdapter<RentalMachineEntity> __updateAdapterOfRentalMachineEntity;

  private final EntityDeletionOrUpdateAdapter<RentalEntity> __updateAdapterOfRentalEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRentalStatus;

  public RentalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRentalMachineEntity = new EntityInsertionAdapter<RentalMachineEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rental_machines` (`id`,`shopId`,`machineName`,`rentPrice`,`deposit`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentalMachineEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getShopId());
        statement.bindString(3, entity.getMachineName());
        statement.bindDouble(4, entity.getRentPrice());
        statement.bindDouble(5, entity.getDeposit());
      }
    };
    this.__insertionAdapterOfRentalEntity = new EntityInsertionAdapter<RentalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rentals` (`id`,`customerId`,`shopId`,`machineName`,`deposit`,`rentAmount`,`startDate`,`returnDate`,`status`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCustomerId());
        statement.bindLong(3, entity.getShopId());
        statement.bindString(4, entity.getMachineName());
        statement.bindDouble(5, entity.getDeposit());
        statement.bindDouble(6, entity.getRentAmount());
        statement.bindLong(7, entity.getStartDate());
        if (entity.getReturnDate() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getReturnDate());
        }
        statement.bindString(9, entity.getStatus());
      }
    };
    this.__deletionAdapterOfRentalMachineEntity = new EntityDeletionOrUpdateAdapter<RentalMachineEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rental_machines` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentalMachineEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfRentalEntity = new EntityDeletionOrUpdateAdapter<RentalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rentals` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentalEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRentalMachineEntity = new EntityDeletionOrUpdateAdapter<RentalMachineEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rental_machines` SET `id` = ?,`shopId` = ?,`machineName` = ?,`rentPrice` = ?,`deposit` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentalMachineEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getShopId());
        statement.bindString(3, entity.getMachineName());
        statement.bindDouble(4, entity.getRentPrice());
        statement.bindDouble(5, entity.getDeposit());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__updateAdapterOfRentalEntity = new EntityDeletionOrUpdateAdapter<RentalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rentals` SET `id` = ?,`customerId` = ?,`shopId` = ?,`machineName` = ?,`deposit` = ?,`rentAmount` = ?,`startDate` = ?,`returnDate` = ?,`status` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RentalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCustomerId());
        statement.bindLong(3, entity.getShopId());
        statement.bindString(4, entity.getMachineName());
        statement.bindDouble(5, entity.getDeposit());
        statement.bindDouble(6, entity.getRentAmount());
        statement.bindLong(7, entity.getStartDate());
        if (entity.getReturnDate() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getReturnDate());
        }
        statement.bindString(9, entity.getStatus());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateRentalStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE rentals SET status = ?, returnDate = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMachine(final RentalMachineEntity machine,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRentalMachineEntity.insertAndReturnId(machine);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertRental(final RentalEntity rental,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRentalEntity.insertAndReturnId(rental);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMachine(final RentalMachineEntity machine,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRentalMachineEntity.handle(machine);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRental(final RentalEntity rental,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRentalEntity.handle(rental);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMachine(final RentalMachineEntity machine,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRentalMachineEntity.handle(machine);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRental(final RentalEntity rental,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRentalEntity.handle(rental);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRentalStatus(final long rentalId, final String status, final Long returnDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRentalStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        if (returnDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, returnDate);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, rentalId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateRentalStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RentalMachineEntity>> getAllMachines(final long shopId) {
    final String _sql = "SELECT * FROM rental_machines WHERE shopId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, shopId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rental_machines"}, new Callable<List<RentalMachineEntity>>() {
      @Override
      @NonNull
      public List<RentalMachineEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfShopId = CursorUtil.getColumnIndexOrThrow(_cursor, "shopId");
          final int _cursorIndexOfMachineName = CursorUtil.getColumnIndexOrThrow(_cursor, "machineName");
          final int _cursorIndexOfRentPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "rentPrice");
          final int _cursorIndexOfDeposit = CursorUtil.getColumnIndexOrThrow(_cursor, "deposit");
          final List<RentalMachineEntity> _result = new ArrayList<RentalMachineEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentalMachineEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpShopId;
            _tmpShopId = _cursor.getLong(_cursorIndexOfShopId);
            final String _tmpMachineName;
            _tmpMachineName = _cursor.getString(_cursorIndexOfMachineName);
            final double _tmpRentPrice;
            _tmpRentPrice = _cursor.getDouble(_cursorIndexOfRentPrice);
            final double _tmpDeposit;
            _tmpDeposit = _cursor.getDouble(_cursorIndexOfDeposit);
            _item = new RentalMachineEntity(_tmpId,_tmpShopId,_tmpMachineName,_tmpRentPrice,_tmpDeposit);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<RentalEntity>> getAllRentals(final long shopId) {
    final String _sql = "SELECT * FROM rentals WHERE shopId = ? ORDER BY startDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, shopId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rentals"}, new Callable<List<RentalEntity>>() {
      @Override
      @NonNull
      public List<RentalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfShopId = CursorUtil.getColumnIndexOrThrow(_cursor, "shopId");
          final int _cursorIndexOfMachineName = CursorUtil.getColumnIndexOrThrow(_cursor, "machineName");
          final int _cursorIndexOfDeposit = CursorUtil.getColumnIndexOrThrow(_cursor, "deposit");
          final int _cursorIndexOfRentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "rentAmount");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<RentalEntity> _result = new ArrayList<RentalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentalEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCustomerId;
            _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
            final long _tmpShopId;
            _tmpShopId = _cursor.getLong(_cursorIndexOfShopId);
            final String _tmpMachineName;
            _tmpMachineName = _cursor.getString(_cursorIndexOfMachineName);
            final double _tmpDeposit;
            _tmpDeposit = _cursor.getDouble(_cursorIndexOfDeposit);
            final double _tmpRentAmount;
            _tmpRentAmount = _cursor.getDouble(_cursorIndexOfRentAmount);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final Long _tmpReturnDate;
            if (_cursor.isNull(_cursorIndexOfReturnDate)) {
              _tmpReturnDate = null;
            } else {
              _tmpReturnDate = _cursor.getLong(_cursorIndexOfReturnDate);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new RentalEntity(_tmpId,_tmpCustomerId,_tmpShopId,_tmpMachineName,_tmpDeposit,_tmpRentAmount,_tmpStartDate,_tmpReturnDate,_tmpStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<RentalEntity>> getRentalsByStatus(final long shopId, final String status) {
    final String _sql = "SELECT * FROM rentals WHERE shopId = ? AND status = ? ORDER BY startDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, shopId);
    _argIndex = 2;
    _statement.bindString(_argIndex, status);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rentals"}, new Callable<List<RentalEntity>>() {
      @Override
      @NonNull
      public List<RentalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfShopId = CursorUtil.getColumnIndexOrThrow(_cursor, "shopId");
          final int _cursorIndexOfMachineName = CursorUtil.getColumnIndexOrThrow(_cursor, "machineName");
          final int _cursorIndexOfDeposit = CursorUtil.getColumnIndexOrThrow(_cursor, "deposit");
          final int _cursorIndexOfRentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "rentAmount");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<RentalEntity> _result = new ArrayList<RentalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentalEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCustomerId;
            _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
            final long _tmpShopId;
            _tmpShopId = _cursor.getLong(_cursorIndexOfShopId);
            final String _tmpMachineName;
            _tmpMachineName = _cursor.getString(_cursorIndexOfMachineName);
            final double _tmpDeposit;
            _tmpDeposit = _cursor.getDouble(_cursorIndexOfDeposit);
            final double _tmpRentAmount;
            _tmpRentAmount = _cursor.getDouble(_cursorIndexOfRentAmount);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final Long _tmpReturnDate;
            if (_cursor.isNull(_cursorIndexOfReturnDate)) {
              _tmpReturnDate = null;
            } else {
              _tmpReturnDate = _cursor.getLong(_cursorIndexOfReturnDate);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new RentalEntity(_tmpId,_tmpCustomerId,_tmpShopId,_tmpMachineName,_tmpDeposit,_tmpRentAmount,_tmpStartDate,_tmpReturnDate,_tmpStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getActiveRentalCount(final long shopId) {
    final String _sql = "SELECT COUNT(*) FROM rentals WHERE shopId = ? AND status = 'ACTIVE'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, shopId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rentals"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getTotalDepositsHeld(final long shopId) {
    final String _sql = "SELECT SUM(deposit) FROM rentals WHERE shopId = ? AND status = 'ACTIVE'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, shopId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rentals"}, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<RentalEntity>> getRentalsByCustomer(final long customerId) {
    final String _sql = "SELECT * FROM rentals WHERE customerId = ? ORDER BY startDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, customerId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rentals"}, new Callable<List<RentalEntity>>() {
      @Override
      @NonNull
      public List<RentalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfShopId = CursorUtil.getColumnIndexOrThrow(_cursor, "shopId");
          final int _cursorIndexOfMachineName = CursorUtil.getColumnIndexOrThrow(_cursor, "machineName");
          final int _cursorIndexOfDeposit = CursorUtil.getColumnIndexOrThrow(_cursor, "deposit");
          final int _cursorIndexOfRentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "rentAmount");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<RentalEntity> _result = new ArrayList<RentalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentalEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCustomerId;
            _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
            final long _tmpShopId;
            _tmpShopId = _cursor.getLong(_cursorIndexOfShopId);
            final String _tmpMachineName;
            _tmpMachineName = _cursor.getString(_cursorIndexOfMachineName);
            final double _tmpDeposit;
            _tmpDeposit = _cursor.getDouble(_cursorIndexOfDeposit);
            final double _tmpRentAmount;
            _tmpRentAmount = _cursor.getDouble(_cursorIndexOfRentAmount);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final Long _tmpReturnDate;
            if (_cursor.isNull(_cursorIndexOfReturnDate)) {
              _tmpReturnDate = null;
            } else {
              _tmpReturnDate = _cursor.getLong(_cursorIndexOfReturnDate);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new RentalEntity(_tmpId,_tmpCustomerId,_tmpShopId,_tmpMachineName,_tmpDeposit,_tmpRentAmount,_tmpStartDate,_tmpReturnDate,_tmpStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getOverdueRentals(final long shopId, final long now,
      final Continuation<? super List<RentalEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM rentals \n"
            + "        WHERE shopId = ? AND status = 'ACTIVE' \n"
            + "        AND returnDate IS NOT NULL AND returnDate < ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, shopId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, now);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RentalEntity>>() {
      @Override
      @NonNull
      public List<RentalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfShopId = CursorUtil.getColumnIndexOrThrow(_cursor, "shopId");
          final int _cursorIndexOfMachineName = CursorUtil.getColumnIndexOrThrow(_cursor, "machineName");
          final int _cursorIndexOfDeposit = CursorUtil.getColumnIndexOrThrow(_cursor, "deposit");
          final int _cursorIndexOfRentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "rentAmount");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<RentalEntity> _result = new ArrayList<RentalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RentalEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCustomerId;
            _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
            final long _tmpShopId;
            _tmpShopId = _cursor.getLong(_cursorIndexOfShopId);
            final String _tmpMachineName;
            _tmpMachineName = _cursor.getString(_cursorIndexOfMachineName);
            final double _tmpDeposit;
            _tmpDeposit = _cursor.getDouble(_cursorIndexOfDeposit);
            final double _tmpRentAmount;
            _tmpRentAmount = _cursor.getDouble(_cursorIndexOfRentAmount);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final Long _tmpReturnDate;
            if (_cursor.isNull(_cursorIndexOfReturnDate)) {
              _tmpReturnDate = null;
            } else {
              _tmpReturnDate = _cursor.getLong(_cursorIndexOfReturnDate);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new RentalEntity(_tmpId,_tmpCustomerId,_tmpShopId,_tmpMachineName,_tmpDeposit,_tmpRentAmount,_tmpStartDate,_tmpReturnDate,_tmpStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
