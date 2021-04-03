CREATE TABLE LINEITEM (
  orderkey       INT,
  partkey        INT,
  suppkey        INT,
  linenumber     INT,
  quantity       DECIMAL,
  extendedprice  DECIMAL,
  discount       DECIMAL,
  tax            DECIMAL,
  returnflag     CHAR(1),
  linestatus     CHAR(1),
  shipdate       DATE,
  commitdate     DATE,
  receiptdate    DATE,
  shipinstruct   CHAR(25),
  shipmode       CHAR(10),
  comment        VARCHAR(44)
);

CREATE TABLE ORDERS (
  orderkey       INT,
  custkey        INT,
  orderstatus    CHAR(1),
  totalprice     DECIMAL,
  orderdate      DATE,
  orderpriority  CHAR(15),
  clerk          CHAR(15),
  shippriority   INT,
  comment        VARCHAR(79)
);

CREATE TABLE PART (
  partkey      INT,
  name         VARCHAR(55),
  mfgr         CHAR(25),
  brand        CHAR(10),
  type         VARCHAR(25),
  size         INT,
  container    CHAR(10),
  retailprice  DECIMAL,
  comment      VARCHAR(23)
);

CREATE TABLE CUSTOMER (
  custkey      INT,
  name         VARCHAR(25),
  address      VARCHAR(40),
  nationkey    INT,
  phone        CHAR(15),
  acctbal      DECIMAL,
  mktsegment   CHAR(10),
  comment      VARCHAR(117)
);

CREATE TABLE SUPPLIER (
  suppkey      INT,
  name         CHAR(25),
  address      VARCHAR(40),
  nationkey    INT,
  phone        CHAR(15),
  acctbal      DECIMAL,
  comment      VARCHAR(101)
);

CREATE TABLE PARTSUPP (
  partkey      INT,
  suppkey      INT,
  availqty     INT,
  supplycost   DECIMAL,
  comment      VARCHAR(199)
);

CREATE TABLE NATION (
  nationkey    INT,
  name         CHAR(25),
  regionkey    INT,
  comment      VARCHAR(152)
);

CREATE TABLE REGION (
  regionkey    INT,
  name         CHAR(25),
  comment      VARCHAR(152)
);

SELECT AVG(l.extendedprice*l.discount) AS revenue
FROM   lineitem l
WHERE  l.shipdate >= DATE('1994-01-01')
  AND  l.shipdate < DATE('1995-01-01')
  AND  (l.discount BETWEEN (0.06 - 0.01) AND (0.06 + 0.01))
  AND  l.quantity < 24;


SELECT returnflag, linestatus,
  SUM(quantity) AS sum_qty,
  SUM(extendedprice) AS sum_base_price,
  SUM(extendedprice * (1-discount)) AS sum_disc_price,
  SUM(extendedprice * (1-discount)*(1+tax)) AS sum_charge,
  AVG(quantity) AS avg_qty,
  AVG(extendedprice) AS avg_price,
  AVG(discount) AS avg_disc,
  COUNT(*) AS count_order
FROM lineitem
WHERE shipdate <= DATE('1997-09-01')
GROUP BY returnflag, linestatus;

SELECT  c.custkey, c.name,
        c.acctbal,
        n.name,
        c.address,
        c.phone,
        c.comment,
        SUM(l.extendedprice * (1 - l.discount)) AS revenue
FROM    customer c, orders o, lineitem, nation n
WHERE   c.custkey = o.custkey
  AND   l.orderkey = o.orderkey
  AND   o.orderdate >= DATE('1993-10-01')
  AND   o.orderdate < DATE('1994-01-01')
  AND   l.returnflag = 'R'
  AND   c.nationkey = n.nationkey
GROUP BY c.custkey, c.name;

SELECT ORDERS.orderkey,
       ORDERS.orderdate,
       ORDERS.shippriority,
       SUM(extendedprice * (1 - discount)) AS query3
FROM   CUSTOMER, ORDERS, LINEITEM
WHERE  CUSTOMER.mktsegment = 'BUILDING'
  AND  ORDERS.custkey = CUSTOMER.custkey
  AND  LINEITEM.orderkey = ORDERS.orderkey
  AND  ORDERS.orderdate < DATE('1995-03-15')
  AND  LINEITEM.shipdate > DATE('1995-03-15')
GROUP BY ORDERS.orderkey, ORDERS.orderdate;


SELECT SUM(l.extendedprice * (1 - l.discount) ) AS revenue
FROM lineitem l, part p
WHERE
(
    p.partkey = l.partkey
    AND p.brand = 'Brand#12'
    AND ( p.container IN LIST ( 'SM CASE', 'SM BOX', 'SM PACK', 'SM PKG') )
    AND l.quantity >= 1 AND l.quantity <= 1 + 10
    AND ( p.size >= 1 AND p.size <= 5)
    AND (l.shipmode IN LIST ('AIR', 'AIR REG') )
    AND l.shipinstruct = 'DELIVER IN PERSON'
)
OR
(
    p.partkey = l.partkey
    AND p.brand = 'Brand#23'
    AND ( p.container IN LIST ('MED BAG', 'MED BOX', 'MED PKG', 'MED PACK') )
    AND l.quantity >= 10 AND l.quantity <= 10 + 10
    AND ( p.size >= 1 AND p.size <= 10)
    AND ( l.shipmode IN LIST ('AIR', 'AIR REG') )
    AND l.shipinstruct = 'DELIVER IN PERSON'
)
OR
(
    p.partkey = l.partkey
    AND p.brand = 'Brand#34'
    AND ( p.container IN LIST ( 'LG CASE', 'LG BOX', 'LG PACK', 'LG PKG') )
    AND l.quantity >= 20 AND l.quantity <= 20 + 10
    AND ( p.size >= 1 AND p.size <= 15 )
    AND ( l.shipmode IN LIST ('AIR', 'AIR REG') )
    AND l.shipinstruct = 'DELIVER IN PERSON'
);
