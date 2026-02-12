package com.aimanager.agent.models;

import com.aimanager.agent.request.FetchedData;
import lombok.Data;

import java.time.Instant;
import java.util.Objects;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("fetched")
public class CFData {

    public static CFData copy(FetchedData ft, CFData cr) {
        KeyClass id = new KeyClass();
        id.timestamp = Instant.now();
        id.node = ft.getNodeId();
        id.key= ft.getKey();
        cr.id = id;
        cr.value = ft.getValue();
        return cr;
    }

    public CFData() { }

    @PrimaryKey
    protected KeyClass id;

    protected String value;

    @PrimaryKeyClass
    public static class KeyClass {

        @PrimaryKeyColumn(ordinal = 0, name = "node", type = PrimaryKeyType.PARTITIONED)
        private long node;

        @PrimaryKeyColumn(ordinal = 1, name = "timestamp", type = PrimaryKeyType.CLUSTERED)
        private Instant timestamp;

        @PrimaryKeyColumn(ordinal = 3, name = "key", type = PrimaryKeyType.CLUSTERED)
        private String key;

        public long getNode() {
            return node;
        }

        public void setNode(long node) {
            this.node = node;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

       /* @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (node ^ (node >>> 32));
            result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
            result = prime * result + (int) (node ^ (node >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            KeyClass other = (KeyClass) obj;
            if (key != other.key)
                return false;
            if (timestamp == null) {
                if (other.timestamp != null)
                    return false;
            } else if (!timestamp.equals(other.timestamp))
                return false;
            if (node != other.node)
                return false;
            return true;
        }*/

        @Override
        public int hashCode() {
            return Objects.hash(node, timestamp, key);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof KeyClass)) return false;
            KeyClass that = (KeyClass) o;
            return node == that.node &&
                    Objects.equals(timestamp, that.timestamp) &&
                    Objects.equals(key, that.key);
        }

    }

    public KeyClass getId() {
        return id;
    }

    public void setId(KeyClass id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("rawtypes")
        CFData other = (CFData) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
