SELECT
  T1.PLACE_ID    placeId,
  T1.PLACE_CODE  placeCode,
  T1.PLACE_NAME  placeName,
  T1.AREA        area,
  T1.DESCRIPTION description,
  T1.STATUS      status
FROM QLDV_PLACE T1
WHERE T1.STATUS = 1