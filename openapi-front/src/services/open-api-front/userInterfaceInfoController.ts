// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addInterfaceInfo POST /api/userinterfaceinfo/add */
export async function addInterfaceInfoUsingPost1(
  body: API.UserInterfaceInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponselong>('/api/userinterfaceinfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** addLeftNum POST /api/userinterfaceinfo/addnum */
export async function addLeftNumUsingPost(
  body: API.UserInterfaceInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/userinterfaceinfo/addnum', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteInterfaceInfo POST /api/userinterfaceinfo/delete */
export async function deleteInterfaceInfoUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/userinterfaceinfo/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getInterfaceInfoById GET /api/userinterfaceinfo/get */
export async function getInterfaceInfoByIdUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInterfaceInfoByIdUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserInterfaceInfo>('/api/userinterfaceinfo/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listInterfaceInfo GET /api/userinterfaceinfo/list */
export async function listInterfaceInfoUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listInterfaceInfoUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListUserInterfaceInfo>('/api/userinterfaceinfo/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listInterfaceInfoByList GET /api/userinterfaceinfo/list/page */
export async function listInterfaceInfoByListUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseListInterfaceNumVO>('/api/userinterfaceinfo/list/page', {
    method: 'GET',
    ...(options || {}),
  });
}

/** updateInterfaceInfo POST /api/userinterfaceinfo/update */
export async function updateInterfaceInfoUsingPost1(
  body: API.UserInterfaceInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/userinterfaceinfo/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
