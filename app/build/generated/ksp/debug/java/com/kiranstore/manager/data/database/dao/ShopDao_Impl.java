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
import com.kiranstore.manager.data.database.entities.ShopEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ShopDao_Impl implements ShopDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ShopEntity> __insertionAdapterOfShopEntity;

  private final EntityDeletionOrUpdateAdapter<ShopEntity> __updateAdapterOfShopEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteShop;

  public ShopDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfShopEntity = new EntityInsertionAdapter<ShopEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `shops` (`id`,`shopName`,`ownerName`,`phone`,`address`,`logoUri`,`userId`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShopEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getShopName());
        statement.bindString(3, entity.getOwnerName());
        statement.bindString(4, entity.getPhone());
        statement.bindString(5, entity.getAddress());
        if (entity.getLogoUri() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLogoUri());
        }
        statement.bindString(7, entity.getUserId());
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__updateAdapterOfShopEntity = new EntityDeletionOrUpdateAdapter<ShopEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `shops` SET `id` = ?,`shopName` = ?,`ownerName` = ?,`phone` = ?,`address` = ?,`logoUri` = ?,`userId` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShopEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getShopName());
        statement.bindString(3, entity.getOwnerName());
        statement.bindString(4, entity.getPhone());
        statement.bindString(5, entity.getAddress());
        if (entity.getLogoUri() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLogoUri());
        }
        statement.bindString(7, entity.getUserId());
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteShop = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM shops WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertShop(final ShopEntity shop, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfShopEntity.insertAndReturnId(shop);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateShop(final ShopEntity shop, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfShopEntity.handle(shop);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteShop(final long shopId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteShop.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, shopId);
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
          __preparedStmtOfDeleteShop.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ShopEntity> getShopByUserId(final String userId) {
    final String _sql = "SELECT * FROM shops WHERE userId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"shops"}, new Callable<ShopEntity>() {
      @Override
      @Nullable
      public ShopEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfShopName = CursorUtil.getColumnIndexOrThrow(_cursor, "shopName");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfLogoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "logoUri");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final ShopEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpShopName;
            _tmpShopName = _cursor.getString(_cursorIndexOfShopName);
            final String _tmpOwnerName;
            _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpAddress;
            _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            final String _tmpLogoUri;
            if (_cursor.isNull(_cursorIndexOfLogoUri)) {
              _tmpLogoUri = null;
            } else {
              _tmpLogoUri = _cursor.getString(_cursorIndexOfLogoUri);
            }
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new ShopEntity(_tmpId,_tmpShopName,_tmpOwnerName,_tmpPhone,_tmpAddress,_tmpLogoUri,_tmpUserId,_tmpCreatedAt);
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
  public Object getShopById(final long shopId, final Continuation<? super ShopEntity> $completion) {
    final String _sql = "SELECT * FROM shops WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, shopId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ShopEntity>() {
      @Override
      @Nullable
      public ShopEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfShopName = CursorUtil.getColumnIndexOrThrow(_cursor, "shopName");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfLogoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "logoUri");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final ShopEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpShopName;
            _tmpShopName = _cursor.getString(_cursorIndexOfShopName);
            final String _tmpOwnerName;
            _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final String _tmpAddress;
            _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            final String _tmpLogoUri;
            if (_cursor.isNull(_cursorIndexOfLogoUri)) {
              _tmpLogoUri = null;
            } else {
              _tmpLogoUri = _cursor.getString(_cursorIndexOfLogoUri);
            }
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new ShopEntity(_tmpId,_tmpShopName,_tmpOwnerName,_tmpPhone,_tmpAddress,_tmpLogoUri,_tmpUserId,_tmpCreatedAt);
          } else {
            _result = null;
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
