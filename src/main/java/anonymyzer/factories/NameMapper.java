package anonymyzer.factories;

import java.util.List;

public interface NameMapper {
	NameMapperOutput getNameSubsitutions(List<String> aNames);
}
