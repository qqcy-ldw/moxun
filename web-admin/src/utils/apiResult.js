/**
 * 从统一 Result 或分页结构中取出列表数组（兼容多种后端/拦截器形态）
 *
 * 可能的 result 形态：
 *   1. { code, data: { total, records: [] } }      ← 拦截器返回的 Result，内层是分页对象
 *   2. { code, data: [] }                          ← 拦截器返回的 Result，内层直接是 List
 *   3. { total, records: [] }                       ← 已被外层拆包，仅剩分页对象
 *   4. []                                           ← 已被外层完全拆包，直接是数组
 */
export function unwrapList(result) {
  if (!result) return []

  // 已是数组（形态 4）
  if (Array.isArray(result)) return result

  // 分页对象（形态 3）
  if (Array.isArray(result.records)) return result.records
  if (Array.isArray(result.list)) return result.list

  const d = result.data
  if (d == null) return []

  // data 直接是数组（形态 2）
  if (Array.isArray(d)) return d

  // data 是分页对象（形态 1）
  if (Array.isArray(d.records)) return d.records
  if (Array.isArray(d.list)) return d.list

  return []
}
