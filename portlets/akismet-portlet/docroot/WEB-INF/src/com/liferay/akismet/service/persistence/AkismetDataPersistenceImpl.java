/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.akismet.service.persistence;

import com.liferay.akismet.NoSuchDataException;
import com.liferay.akismet.model.AkismetData;
import com.liferay.akismet.model.impl.AkismetDataImpl;
import com.liferay.akismet.model.impl.AkismetDataModelImpl;

import com.liferay.portal.NoSuchModelException;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * The persistence implementation for the akismet data service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AkismetDataPersistence
 * @see AkismetDataUtil
 * @generated
 */
public class AkismetDataPersistenceImpl extends BasePersistenceImpl<AkismetData>
	implements AkismetDataPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link AkismetDataUtil} to access the akismet data persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = AkismetDataImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_LTMODIFIEDDATE =
		new FinderPath(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataModelImpl.FINDER_CACHE_ENABLED, AkismetDataImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByLtModifiedDate",
			new String[] {
				Date.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_LTMODIFIEDDATE =
		new FinderPath(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "countByLtModifiedDate",
			new String[] { Date.class.getName() });
	public static final FinderPath FINDER_PATH_FETCH_BY_MBMESSAGEID = new FinderPath(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataModelImpl.FINDER_CACHE_ENABLED, AkismetDataImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByMBMessageId",
			new String[] { Long.class.getName() },
			AkismetDataModelImpl.MBMESSAGEID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_MBMESSAGEID = new FinderPath(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByMBMessageId",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataModelImpl.FINDER_CACHE_ENABLED, AkismetDataImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataModelImpl.FINDER_CACHE_ENABLED, AkismetDataImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	/**
	 * Caches the akismet data in the entity cache if it is enabled.
	 *
	 * @param akismetData the akismet data
	 */
	public void cacheResult(AkismetData akismetData) {
		EntityCacheUtil.putResult(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataImpl.class, akismetData.getPrimaryKey(), akismetData);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_MBMESSAGEID,
			new Object[] { Long.valueOf(akismetData.getMbMessageId()) },
			akismetData);

		akismetData.resetOriginalValues();
	}

	/**
	 * Caches the akismet datas in the entity cache if it is enabled.
	 *
	 * @param akismetDatas the akismet datas
	 */
	public void cacheResult(List<AkismetData> akismetDatas) {
		for (AkismetData akismetData : akismetDatas) {
			if (EntityCacheUtil.getResult(
						AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
						AkismetDataImpl.class, akismetData.getPrimaryKey()) == null) {
				cacheResult(akismetData);
			}
			else {
				akismetData.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all akismet datas.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(AkismetDataImpl.class.getName());
		}

		EntityCacheUtil.clearCache(AkismetDataImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the akismet data.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(AkismetData akismetData) {
		EntityCacheUtil.removeResult(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataImpl.class, akismetData.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(akismetData);
	}

	@Override
	public void clearCache(List<AkismetData> akismetDatas) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (AkismetData akismetData : akismetDatas) {
			EntityCacheUtil.removeResult(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
				AkismetDataImpl.class, akismetData.getPrimaryKey());

			clearUniqueFindersCache(akismetData);
		}
	}

	protected void clearUniqueFindersCache(AkismetData akismetData) {
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_MBMESSAGEID,
			new Object[] { Long.valueOf(akismetData.getMbMessageId()) });
	}

	/**
	 * Creates a new akismet data with the primary key. Does not add the akismet data to the database.
	 *
	 * @param akismetDataId the primary key for the new akismet data
	 * @return the new akismet data
	 */
	public AkismetData create(long akismetDataId) {
		AkismetData akismetData = new AkismetDataImpl();

		akismetData.setNew(true);
		akismetData.setPrimaryKey(akismetDataId);

		return akismetData;
	}

	/**
	 * Removes the akismet data with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param akismetDataId the primary key of the akismet data
	 * @return the akismet data that was removed
	 * @throws com.liferay.akismet.NoSuchDataException if a akismet data with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData remove(long akismetDataId)
		throws NoSuchDataException, SystemException {
		return remove(Long.valueOf(akismetDataId));
	}

	/**
	 * Removes the akismet data with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the akismet data
	 * @return the akismet data that was removed
	 * @throws com.liferay.akismet.NoSuchDataException if a akismet data with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AkismetData remove(Serializable primaryKey)
		throws NoSuchDataException, SystemException {
		Session session = null;

		try {
			session = openSession();

			AkismetData akismetData = (AkismetData)session.get(AkismetDataImpl.class,
					primaryKey);

			if (akismetData == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchDataException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(akismetData);
		}
		catch (NoSuchDataException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected AkismetData removeImpl(AkismetData akismetData)
		throws SystemException {
		akismetData = toUnwrappedModel(akismetData);

		Session session = null;

		try {
			session = openSession();

			if (akismetData.isCachedModel()) {
				akismetData = (AkismetData)session.get(AkismetDataImpl.class,
						akismetData.getPrimaryKeyObj());
			}

			session.delete(akismetData);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		clearCache(akismetData);

		return akismetData;
	}

	@Override
	public AkismetData updateImpl(
		com.liferay.akismet.model.AkismetData akismetData)
		throws SystemException {
		akismetData = toUnwrappedModel(akismetData);

		boolean isNew = akismetData.isNew();

		AkismetDataModelImpl akismetDataModelImpl = (AkismetDataModelImpl)akismetData;

		Session session = null;

		try {
			session = openSession();

			if (akismetData.isNew()) {
				session.save(akismetData);

				akismetData.setNew(false);
			}
			else {
				session.merge(akismetData);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !AkismetDataModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		EntityCacheUtil.putResult(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
			AkismetDataImpl.class, akismetData.getPrimaryKey(), akismetData);

		if (isNew) {
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_MBMESSAGEID,
				new Object[] { Long.valueOf(akismetData.getMbMessageId()) },
				akismetData);
		}
		else {
			if ((akismetDataModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_MBMESSAGEID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(akismetDataModelImpl.getOriginalMbMessageId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_MBMESSAGEID,
					args);

				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_MBMESSAGEID,
					args);

				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_MBMESSAGEID,
					new Object[] { Long.valueOf(akismetData.getMbMessageId()) },
					akismetData);
			}
		}

		return akismetData;
	}

	protected AkismetData toUnwrappedModel(AkismetData akismetData) {
		if (akismetData instanceof AkismetDataImpl) {
			return akismetData;
		}

		AkismetDataImpl akismetDataImpl = new AkismetDataImpl();

		akismetDataImpl.setNew(akismetData.isNew());
		akismetDataImpl.setPrimaryKey(akismetData.getPrimaryKey());

		akismetDataImpl.setAkismetDataId(akismetData.getAkismetDataId());
		akismetDataImpl.setModifiedDate(akismetData.getModifiedDate());
		akismetDataImpl.setMbMessageId(akismetData.getMbMessageId());
		akismetDataImpl.setType(akismetData.getType());
		akismetDataImpl.setPermalink(akismetData.getPermalink());
		akismetDataImpl.setReferrer(akismetData.getReferrer());
		akismetDataImpl.setUserAgent(akismetData.getUserAgent());
		akismetDataImpl.setUserIP(akismetData.getUserIP());
		akismetDataImpl.setUserURL(akismetData.getUserURL());

		return akismetDataImpl;
	}

	/**
	 * Returns the akismet data with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the akismet data
	 * @return the akismet data
	 * @throws com.liferay.portal.NoSuchModelException if a akismet data with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AkismetData findByPrimaryKey(Serializable primaryKey)
		throws NoSuchModelException, SystemException {
		return findByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the akismet data with the primary key or throws a {@link com.liferay.akismet.NoSuchDataException} if it could not be found.
	 *
	 * @param akismetDataId the primary key of the akismet data
	 * @return the akismet data
	 * @throws com.liferay.akismet.NoSuchDataException if a akismet data with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData findByPrimaryKey(long akismetDataId)
		throws NoSuchDataException, SystemException {
		AkismetData akismetData = fetchByPrimaryKey(akismetDataId);

		if (akismetData == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + akismetDataId);
			}

			throw new NoSuchDataException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				akismetDataId);
		}

		return akismetData;
	}

	/**
	 * Returns the akismet data with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the akismet data
	 * @return the akismet data, or <code>null</code> if a akismet data with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public AkismetData fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		return fetchByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the akismet data with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param akismetDataId the primary key of the akismet data
	 * @return the akismet data, or <code>null</code> if a akismet data with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData fetchByPrimaryKey(long akismetDataId)
		throws SystemException {
		AkismetData akismetData = (AkismetData)EntityCacheUtil.getResult(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
				AkismetDataImpl.class, akismetDataId);

		if (akismetData == _nullAkismetData) {
			return null;
		}

		if (akismetData == null) {
			Session session = null;

			boolean hasException = false;

			try {
				session = openSession();

				akismetData = (AkismetData)session.get(AkismetDataImpl.class,
						Long.valueOf(akismetDataId));
			}
			catch (Exception e) {
				hasException = true;

				throw processException(e);
			}
			finally {
				if (akismetData != null) {
					cacheResult(akismetData);
				}
				else if (!hasException) {
					EntityCacheUtil.putResult(AkismetDataModelImpl.ENTITY_CACHE_ENABLED,
						AkismetDataImpl.class, akismetDataId, _nullAkismetData);
				}

				closeSession(session);
			}
		}

		return akismetData;
	}

	/**
	 * Returns all the akismet datas where modifiedDate &lt; &#63;.
	 *
	 * @param modifiedDate the modified date
	 * @return the matching akismet datas
	 * @throws SystemException if a system exception occurred
	 */
	public List<AkismetData> findByLtModifiedDate(Date modifiedDate)
		throws SystemException {
		return findByLtModifiedDate(modifiedDate, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the akismet datas where modifiedDate &lt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param modifiedDate the modified date
	 * @param start the lower bound of the range of akismet datas
	 * @param end the upper bound of the range of akismet datas (not inclusive)
	 * @return the range of matching akismet datas
	 * @throws SystemException if a system exception occurred
	 */
	public List<AkismetData> findByLtModifiedDate(Date modifiedDate, int start,
		int end) throws SystemException {
		return findByLtModifiedDate(modifiedDate, start, end, null);
	}

	/**
	 * Returns an ordered range of all the akismet datas where modifiedDate &lt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param modifiedDate the modified date
	 * @param start the lower bound of the range of akismet datas
	 * @param end the upper bound of the range of akismet datas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching akismet datas
	 * @throws SystemException if a system exception occurred
	 */
	public List<AkismetData> findByLtModifiedDate(Date modifiedDate, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_LTMODIFIEDDATE;
		finderArgs = new Object[] { modifiedDate, start, end, orderByComparator };

		List<AkismetData> list = (List<AkismetData>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (AkismetData akismetData : list) {
				if (!Validator.equals(modifiedDate,
							akismetData.getModifiedDate())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(2);
			}

			query.append(_SQL_SELECT_AKISMETDATA_WHERE);

			if (modifiedDate == null) {
				query.append(_FINDER_COLUMN_LTMODIFIEDDATE_MODIFIEDDATE_1);
			}
			else {
				query.append(_FINDER_COLUMN_LTMODIFIEDDATE_MODIFIEDDATE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (modifiedDate != null) {
					qPos.add(CalendarUtil.getTimestamp(modifiedDate));
				}

				list = (List<AkismetData>)QueryUtil.list(q, getDialect(),
						start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first akismet data in the ordered set where modifiedDate &lt; &#63;.
	 *
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching akismet data
	 * @throws com.liferay.akismet.NoSuchDataException if a matching akismet data could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData findByLtModifiedDate_First(Date modifiedDate,
		OrderByComparator orderByComparator)
		throws NoSuchDataException, SystemException {
		AkismetData akismetData = fetchByLtModifiedDate_First(modifiedDate,
				orderByComparator);

		if (akismetData != null) {
			return akismetData;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("modifiedDate=");
		msg.append(modifiedDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDataException(msg.toString());
	}

	/**
	 * Returns the first akismet data in the ordered set where modifiedDate &lt; &#63;.
	 *
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching akismet data, or <code>null</code> if a matching akismet data could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData fetchByLtModifiedDate_First(Date modifiedDate,
		OrderByComparator orderByComparator) throws SystemException {
		List<AkismetData> list = findByLtModifiedDate(modifiedDate, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last akismet data in the ordered set where modifiedDate &lt; &#63;.
	 *
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching akismet data
	 * @throws com.liferay.akismet.NoSuchDataException if a matching akismet data could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData findByLtModifiedDate_Last(Date modifiedDate,
		OrderByComparator orderByComparator)
		throws NoSuchDataException, SystemException {
		AkismetData akismetData = fetchByLtModifiedDate_Last(modifiedDate,
				orderByComparator);

		if (akismetData != null) {
			return akismetData;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("modifiedDate=");
		msg.append(modifiedDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchDataException(msg.toString());
	}

	/**
	 * Returns the last akismet data in the ordered set where modifiedDate &lt; &#63;.
	 *
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching akismet data, or <code>null</code> if a matching akismet data could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData fetchByLtModifiedDate_Last(Date modifiedDate,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByLtModifiedDate(modifiedDate);

		List<AkismetData> list = findByLtModifiedDate(modifiedDate, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the akismet datas before and after the current akismet data in the ordered set where modifiedDate &lt; &#63;.
	 *
	 * @param akismetDataId the primary key of the current akismet data
	 * @param modifiedDate the modified date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next akismet data
	 * @throws com.liferay.akismet.NoSuchDataException if a akismet data with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData[] findByLtModifiedDate_PrevAndNext(long akismetDataId,
		Date modifiedDate, OrderByComparator orderByComparator)
		throws NoSuchDataException, SystemException {
		AkismetData akismetData = findByPrimaryKey(akismetDataId);

		Session session = null;

		try {
			session = openSession();

			AkismetData[] array = new AkismetDataImpl[3];

			array[0] = getByLtModifiedDate_PrevAndNext(session, akismetData,
					modifiedDate, orderByComparator, true);

			array[1] = akismetData;

			array[2] = getByLtModifiedDate_PrevAndNext(session, akismetData,
					modifiedDate, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected AkismetData getByLtModifiedDate_PrevAndNext(Session session,
		AkismetData akismetData, Date modifiedDate,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_AKISMETDATA_WHERE);

		if (modifiedDate == null) {
			query.append(_FINDER_COLUMN_LTMODIFIEDDATE_MODIFIEDDATE_1);
		}
		else {
			query.append(_FINDER_COLUMN_LTMODIFIEDDATE_MODIFIEDDATE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (modifiedDate != null) {
			qPos.add(CalendarUtil.getTimestamp(modifiedDate));
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(akismetData);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<AkismetData> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the akismet data where mbMessageId = &#63; or throws a {@link com.liferay.akismet.NoSuchDataException} if it could not be found.
	 *
	 * @param mbMessageId the mb message ID
	 * @return the matching akismet data
	 * @throws com.liferay.akismet.NoSuchDataException if a matching akismet data could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData findByMBMessageId(long mbMessageId)
		throws NoSuchDataException, SystemException {
		AkismetData akismetData = fetchByMBMessageId(mbMessageId);

		if (akismetData == null) {
			StringBundler msg = new StringBundler(4);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("mbMessageId=");
			msg.append(mbMessageId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchDataException(msg.toString());
		}

		return akismetData;
	}

	/**
	 * Returns the akismet data where mbMessageId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param mbMessageId the mb message ID
	 * @return the matching akismet data, or <code>null</code> if a matching akismet data could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData fetchByMBMessageId(long mbMessageId)
		throws SystemException {
		return fetchByMBMessageId(mbMessageId, true);
	}

	/**
	 * Returns the akismet data where mbMessageId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param mbMessageId the mb message ID
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching akismet data, or <code>null</code> if a matching akismet data could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData fetchByMBMessageId(long mbMessageId,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { mbMessageId };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_MBMESSAGEID,
					finderArgs, this);
		}

		if (result instanceof AkismetData) {
			AkismetData akismetData = (AkismetData)result;

			if ((mbMessageId != akismetData.getMbMessageId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_SELECT_AKISMETDATA_WHERE);

			query.append(_FINDER_COLUMN_MBMESSAGEID_MBMESSAGEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(mbMessageId);

				List<AkismetData> list = q.list();

				result = list;

				AkismetData akismetData = null;

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_MBMESSAGEID,
						finderArgs, list);
				}
				else {
					akismetData = list.get(0);

					cacheResult(akismetData);

					if ((akismetData.getMbMessageId() != mbMessageId)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_MBMESSAGEID,
							finderArgs, akismetData);
					}
				}

				return akismetData;
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (result == null) {
					FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_MBMESSAGEID,
						finderArgs);
				}

				closeSession(session);
			}
		}
		else {
			if (result instanceof List<?>) {
				return null;
			}
			else {
				return (AkismetData)result;
			}
		}
	}

	/**
	 * Returns all the akismet datas.
	 *
	 * @return the akismet datas
	 * @throws SystemException if a system exception occurred
	 */
	public List<AkismetData> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the akismet datas.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of akismet datas
	 * @param end the upper bound of the range of akismet datas (not inclusive)
	 * @return the range of akismet datas
	 * @throws SystemException if a system exception occurred
	 */
	public List<AkismetData> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the akismet datas.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of akismet datas
	 * @param end the upper bound of the range of akismet datas (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of akismet datas
	 * @throws SystemException if a system exception occurred
	 */
	public List<AkismetData> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = new Object[] { start, end, orderByComparator };

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<AkismetData> list = (List<AkismetData>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_AKISMETDATA);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_AKISMETDATA;
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (orderByComparator == null) {
					list = (List<AkismetData>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);
				}
				else {
					list = (List<AkismetData>)QueryUtil.list(q, getDialect(),
							start, end);
				}
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the akismet datas where modifiedDate &lt; &#63; from the database.
	 *
	 * @param modifiedDate the modified date
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByLtModifiedDate(Date modifiedDate)
		throws SystemException {
		for (AkismetData akismetData : findByLtModifiedDate(modifiedDate)) {
			remove(akismetData);
		}
	}

	/**
	 * Removes the akismet data where mbMessageId = &#63; from the database.
	 *
	 * @param mbMessageId the mb message ID
	 * @return the akismet data that was removed
	 * @throws SystemException if a system exception occurred
	 */
	public AkismetData removeByMBMessageId(long mbMessageId)
		throws NoSuchDataException, SystemException {
		AkismetData akismetData = findByMBMessageId(mbMessageId);

		return remove(akismetData);
	}

	/**
	 * Removes all the akismet datas from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	public void removeAll() throws SystemException {
		for (AkismetData akismetData : findAll()) {
			remove(akismetData);
		}
	}

	/**
	 * Returns the number of akismet datas where modifiedDate &lt; &#63;.
	 *
	 * @param modifiedDate the modified date
	 * @return the number of matching akismet datas
	 * @throws SystemException if a system exception occurred
	 */
	public int countByLtModifiedDate(Date modifiedDate)
		throws SystemException {
		Object[] finderArgs = new Object[] { modifiedDate };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_LTMODIFIEDDATE,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_AKISMETDATA_WHERE);

			if (modifiedDate == null) {
				query.append(_FINDER_COLUMN_LTMODIFIEDDATE_MODIFIEDDATE_1);
			}
			else {
				query.append(_FINDER_COLUMN_LTMODIFIEDDATE_MODIFIEDDATE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (modifiedDate != null) {
					qPos.add(CalendarUtil.getTimestamp(modifiedDate));
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_LTMODIFIEDDATE,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of akismet datas where mbMessageId = &#63;.
	 *
	 * @param mbMessageId the mb message ID
	 * @return the number of matching akismet datas
	 * @throws SystemException if a system exception occurred
	 */
	public int countByMBMessageId(long mbMessageId) throws SystemException {
		Object[] finderArgs = new Object[] { mbMessageId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_MBMESSAGEID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_AKISMETDATA_WHERE);

			query.append(_FINDER_COLUMN_MBMESSAGEID_MBMESSAGEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(mbMessageId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_MBMESSAGEID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of akismet datas.
	 *
	 * @return the number of akismet datas
	 * @throws SystemException if a system exception occurred
	 */
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_AKISMETDATA);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the akismet data persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.liferay.akismet.model.AkismetData")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<AkismetData>> listenersList = new ArrayList<ModelListener<AkismetData>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<AkismetData>)InstanceFactory.newInstance(
							listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(AkismetDataImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@BeanReference(type = AkismetDataPersistence.class)
	protected AkismetDataPersistence akismetDataPersistence;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private static final String _SQL_SELECT_AKISMETDATA = "SELECT akismetData FROM AkismetData akismetData";
	private static final String _SQL_SELECT_AKISMETDATA_WHERE = "SELECT akismetData FROM AkismetData akismetData WHERE ";
	private static final String _SQL_COUNT_AKISMETDATA = "SELECT COUNT(akismetData) FROM AkismetData akismetData";
	private static final String _SQL_COUNT_AKISMETDATA_WHERE = "SELECT COUNT(akismetData) FROM AkismetData akismetData WHERE ";
	private static final String _FINDER_COLUMN_LTMODIFIEDDATE_MODIFIEDDATE_1 = "akismetData.modifiedDate < NULL";
	private static final String _FINDER_COLUMN_LTMODIFIEDDATE_MODIFIEDDATE_2 = "akismetData.modifiedDate < ?";
	private static final String _FINDER_COLUMN_MBMESSAGEID_MBMESSAGEID_2 = "akismetData.mbMessageId = ?";
	private static final String _ORDER_BY_ENTITY_ALIAS = "akismetData.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No AkismetData exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No AkismetData exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(AkismetDataPersistenceImpl.class);
	private static AkismetData _nullAkismetData = new AkismetDataImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<AkismetData> toCacheModel() {
				return _nullAkismetDataCacheModel;
			}
		};

	private static CacheModel<AkismetData> _nullAkismetDataCacheModel = new CacheModel<AkismetData>() {
			public AkismetData toEntityModel() {
				return _nullAkismetData;
			}
		};
}