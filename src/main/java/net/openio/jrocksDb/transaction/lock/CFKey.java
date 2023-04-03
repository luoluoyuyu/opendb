package net.openio.jrocksDb.transaction.lock;

import lombok.AllArgsConstructor;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.db.Key;

import java.util.Objects;

@AllArgsConstructor
public class CFKey {

    ColumnFamilyId cfId;

    Key key;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CFKey cfKey = (CFKey) o;
        return Objects.equals(cfId, cfKey.cfId) && Objects.equals(key, cfKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cfId, key);
    }
}
