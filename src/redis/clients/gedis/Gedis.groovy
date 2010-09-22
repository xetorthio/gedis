package redis.clients.gedis

import redis.clients.jedis.*

class Gedis  {
	
	@Delegate Jedis jedis
	
	Gedis(Map map) {
		if(map['shardInfo']) {
			this.jedis = new Jedis(map['shardInfo'])
		} else if(map['timeout']) {
			this.jedis = new Jedis(map['host'] as String, map['port'] as int, map['timeout'] as int)
		} else if (map['port']) {
			this.jedis = new Jedis(map['host'] as String, map['port'] as int)
		} else if (map['host']) {
			this.jedis = new Jedis(map['host'] as String)
		}
	}
	
	String getAt(String key) {
		return this.get(key)
	}
	
	void putAt(String key, String value) {
		this.set(key, value)
	}    
	
	def withTransaction(Closure c) {
		def results = null
		try {
			def tx = multi()
			c.delegate = tx
			c.call(tx)
			results = tx.exec()
		} catch(e) {
			tx.discard()
		}
		return result
	}
	
	def withPipeline(Closure c) {
		
	}
}