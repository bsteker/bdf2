package com.bstek.bdf2.core.cache;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.TerracottaClientConfiguration;
import net.sf.ehcache.config.TerracottaConfiguration;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.bdf2.core.context.ContextHolder;

/**
 * 缓存工具类，这里使用的是EHCache为缓存实现，可以实现向缓存当中添加对象、更新对象、删除对象
 * @since 2013-1-29
 * @author Jacky.gao
 */
public class ApplicationCacheImpl implements InitializingBean, ApplicationCache{
	private Cache bdfCache;
	private Cache bdfTemporaryCache;
	private CacheManager cacheManager;
	private String BDF_CACHE_NAME="bdf2.application.cache";
	private String BDF_TEMPORARY_CACHE_NAME="bdf2.temporary.cache";
	private String storePath;
	private String terracottaServer;
	private String applicationName="bdf";
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws IOException, CacheException {
		BDF_CACHE_NAME=buildKey(BDF_CACHE_NAME);
		BDF_TEMPORARY_CACHE_NAME=buildKey(BDF_TEMPORARY_CACHE_NAME);
		storePath=ContextHolder.getBdfTempFileStorePath();
		File f=new File(storePath);
		if(!f.exists()){
			f.mkdirs();
		}
		Configuration config=null;
		if(StringUtils.isNotEmpty(terracottaServer)){
			/**
			 * 如果terracottaServer不为空，那么就采用terracotta server提供的集群式缓存策略，否则就采用单机缓存策略
			 * */
			config=new Configuration();
			TerracottaClientConfiguration terracottaConfig=new TerracottaClientConfiguration();
			terracottaConfig.setUrl(terracottaServer);
			config.addTerracottaConfig(terracottaConfig);
			
			CacheConfiguration cacheConfig=new CacheConfiguration();
			cacheConfig.setEternal(true);
			TerracottaConfiguration terracottaConfiguration=new TerracottaConfiguration();
			terracottaConfiguration.setSynchronousWrites(false);
			cacheConfig.addTerracotta(terracottaConfiguration);
			cacheConfig.setName(BDF_CACHE_NAME);
			cacheConfig.setMaxEntriesLocalHeap(5000);
			cacheConfig.setMaxElementsOnDisk(0);
			cacheConfig.setMemoryStoreEvictionPolicy("LFU");
			config.addCache(cacheConfig);
			
			CacheConfiguration temporaryCacheConfig=new CacheConfiguration();
			temporaryCacheConfig.addTerracotta(terracottaConfiguration);
			temporaryCacheConfig.setEternal(false);
			temporaryCacheConfig.setTimeToIdleSeconds(1800);
			temporaryCacheConfig.setTimeToLiveSeconds(1800);
			temporaryCacheConfig.setDiskExpiryThreadIntervalSeconds(1000);
			temporaryCacheConfig.setName(BDF_TEMPORARY_CACHE_NAME);
			temporaryCacheConfig.setMaxEntriesLocalHeap(5000);
			temporaryCacheConfig.setMaxElementsOnDisk(0);
			temporaryCacheConfig.setMemoryStoreEvictionPolicy("LFU");
			config.addCache(temporaryCacheConfig);
		}else{
			config=new Configuration();
			CacheConfiguration cacheConfig=new CacheConfiguration();
			cacheConfig.setEternal(true);
			cacheConfig.setName(BDF_CACHE_NAME);
			cacheConfig.setMaxEntriesLocalHeap(5000);
			cacheConfig.setMaxElementsOnDisk(0);
			cacheConfig.setMemoryStoreEvictionPolicy("LRU");
			cacheConfig.setDiskSpoolBufferSizeMB(100);
			config.addCache(cacheConfig);
			
			CacheConfiguration temporaryCacheConfig=new CacheConfiguration();
			temporaryCacheConfig.setEternal(false);
			temporaryCacheConfig.setTimeToIdleSeconds(1800);
			temporaryCacheConfig.setTimeToLiveSeconds(1800);
			temporaryCacheConfig.setDiskExpiryThreadIntervalSeconds(1000);
			temporaryCacheConfig.setName(BDF_TEMPORARY_CACHE_NAME);
			temporaryCacheConfig.setMaxEntriesLocalHeap(5000);
			temporaryCacheConfig.setMaxElementsOnDisk(0);
			temporaryCacheConfig.setMemoryStoreEvictionPolicy("LRU");
			config.addCache(temporaryCacheConfig);
		}
		config.setName("cache_manager_"+applicationName);
		this.cacheManager = new CacheManager(config);		
		bdfCache=cacheManager.getCache(BDF_CACHE_NAME);
		bdfTemporaryCache=cacheManager.getCache(BDF_TEMPORARY_CACHE_NAME);
	}
	
	/* (non-Javadoc)
	 * @see com.bstek.bdf2.core.cache.Cache#getCacheObject(java.lang.String)
	 */
	public Object getCacheObject(String key){
		Element objElement=bdfCache.get(buildKey(key));
		if(objElement!=null){
			return objElement.getObjectValue();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.bstek.bdf2.core.cache.Cache#getTemporaryCacheObject(java.lang.String)
	 */
	public Object getTemporaryCacheObject(String key){
		Element objElement=bdfTemporaryCache.get(buildKey(key));
		if(objElement!=null){
			return objElement.getObjectValue();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.bstek.bdf2.core.cache.Cache#putCacheObject(java.lang.String, java.lang.Object)
	 */
	synchronized public void putCacheObject(String key,Object obj){
		bdfCache.put(new Element(buildKey(key),obj));
	}
	
	/* (non-Javadoc)
	 * @see com.bstek.bdf2.core.cache.Cache#putTemporaryCacheObject(java.lang.String, java.lang.Object)
	 */
	synchronized public void putTemporaryCacheObject(String key,Object obj){
		bdfTemporaryCache.put(new Element(buildKey(key),obj));
	}
	
	/* (non-Javadoc)
	 * @see com.bstek.bdf2.core.cache.Cache#removeCacheObject(java.lang.String)
	 */
	public void removeCacheObject(String key){
		bdfCache.remove(buildKey(key));
	}
	
	/* (non-Javadoc)
	 * @see com.bstek.bdf2.core.cache.ApplicationCache#removeCacheObjectsByKeyStartWith(java.lang.String)
	 */
	public void removeCacheObjectsByKeyStartWith(String keyStartWith) {
		List<?> keys=bdfCache.getKeys();
		for(Object obj:keys){
			String key=obj.toString();
			if(key.startsWith(keyStartWith)){
				bdfCache.remove(key);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.bstek.bdf2.core.cache.Cache#removeTemporaryCacheObject(java.lang.String)
	 */
	public void removeTemporaryCacheObject(String key){
		bdfTemporaryCache.remove(buildKey(key));
	}
	
	private String buildKey(String key){
		return key+"_"+this.applicationName;
	}

	public String getTerracottaServer() {
		return terracottaServer;
	}

	public void setTerracottaServer(String terracottaServer) {
		this.terracottaServer = terracottaServer;
	}
}
