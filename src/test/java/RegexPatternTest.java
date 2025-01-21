import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class RegexPatternTest {

	@Test
	void test() {
		String msg = "Cannot add or update a child row: a foreign key constraint fails (`vehicles`.`manufacturer`, CONSTRAINT `FK_MNFR_CITY` FOREIGN KEY (`MNFR_CITY_CODE`) REFERENCES `city` (`CITY_CODE`))";
		Pattern p = Pattern.compile("(?<=(constraint|CONSTRAINT)\\s+`)\\w+");
		Matcher m = p.matcher(msg);
		System.out.println("RegexPatternTest::" + p + "||" + msg);
		while (m.find()) {
			int g = m.groupCount();
			System.out.println("B-----");
			for (int i = 0; i <= g; i++) {
				System.out.println(i + "::" + m.group(i));
			}
			System.out.println("E-----");
		}
	}

}
