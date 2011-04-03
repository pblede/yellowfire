package za.co.yellowfire.domain.result;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

public class ResultTypeConverter implements Converter {
	private static final long serialVersionUID = 1L;
	
	private Session session;
	private DatabaseMapping mapping;
	
	@Override public Object convertObjectValueToDataValue(Object objectValue, Session session) {
		if (objectValue instanceof ResultType) {
			ResultType type = (ResultType) objectValue;
			return (long) type.ordinal() + 1;
		}
		return null;
	}

	@Override
	public Object convertDataValueToObjectValue(Object dataValue, Session session) {
		if (dataValue instanceof Long) {
			Long id = (Long) dataValue;
			for (ResultType type : ResultType.values()) {
				if (type.ordinal() == id.intValue()) {
					return type;
				}
			}
		}
		return null;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override public void initialize(DatabaseMapping mapping, Session session) {
		this.mapping = mapping;
		this.session = session;
	}
}
