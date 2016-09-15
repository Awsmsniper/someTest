package com.qzt360.esTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Map2ES {

	public void map2ES() {
		ESManager esm = new ESManager();
		esm.setup();
		log.info("test map 2 es");
		
		esm.cleanup();
	}
}
