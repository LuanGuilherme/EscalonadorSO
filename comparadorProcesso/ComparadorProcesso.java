package comparadorProcesso;
import bcp.*;
import java.util.*;

public class ComparadorProcesso implements Comparator<BCP> {

	@Override
	public int compare (BCP p1, BCP p2) {

		if (p1.getCreditos() < p2.getCreditos()) {
			return 1;
		} else {
			return -1;
		}

	}

}