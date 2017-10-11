--==============================================================
-- DBMS name:      IBM DB2 UDB 8.x Common Server
-- Created on:     2012/8/24 16:25:41
--==============================================================


alter table BDF_R_ACTION_DEF
   drop foreign key FK_R_PA_R_B_R_EN11;

alter table BDF_R_ACTION_DEF
   drop foreign key FK_R_PA_R_B_R_PA12;

alter table BDF_R_ACTION_DEF_PARAMETER
   drop foreign key FK_R_PA_R_B_R_AC24;

alter table BDF_R_ACTION_DEF_PARAMETER
   drop foreign key FK_R_PA_R_B_R_PA25;

alter table BDF_R_ACTION_DEF_RELATION
   drop foreign key FK_R_PA_R_B_R_ACT;

alter table BDF_R_ACTION_DEF_RELATION
   drop foreign key FK_R_PA_R_B_R_DA1;

alter table BDF_R_ACTION_PARAMETER
   drop foreign key "F_Reference_36";

alter table BDF_R_ACTION_PARAMETER
   drop foreign key "F_Reference_37";

alter table BDF_R_COMPONENT
   drop foreign key FK_R_PA_R_B_R_CO9;

alter table BDF_R_COMPONENT
   drop foreign key FK_R_PA_R_B_R_PA13;

alter table BDF_R_COMPONENT
   drop foreign key "F_Reference_31";

alter table BDF_R_COMPONENT
   drop foreign key FK_R_PA_R_B_R_EN7;

alter table BDF_R_COMPONENT_EVENT
   drop foreign key FK_R_PA_R_B_R_CO5;

alter table BDF_R_COMPONENT_PROPERTY
   drop foreign key FK_R_PA_R_B_R_CO4;

alter table BDF_R_ENTITY
   drop foreign key FK_R_PA_R_B_R_PA14;

alter table BDF_R_ENTITY
   drop foreign key FK_R_PA_R_B_R_EN18;

alter table BDF_R_ENTITY_FIELD
   drop foreign key FK_R_PA_R_B_R_FI8;

alter table BDF_R_ENTITY_FIELD
   drop foreign key "F_Reference_34";

alter table BDF_R_ENTITY_FIELD
   drop foreign key FK_R_PA_R_B_R_EN6;

alter table BDF_R_ENTITY_PARAMETER
   drop foreign key FK_R_PA_R_B_R_EN19;

alter table BDF_R_ENTITY_PARAMETER
   drop foreign key FK_R_PA_R_B_R_PA21;

alter table BDF_R_FIELD_METADATA
   drop foreign key FK_R_PA_R_B_R_PA15;

alter table BDF_R_FIELD_METADATA
   drop foreign key FK_R_PA_R_B_R_ME17;

alter table BDF_R_MAPPING
   drop foreign key FK_R_PA_R_B_R_PA22;

alter table BDF_R_PACKAGE_INFO
   drop foreign key FK_R_PA_R_B_R_PA1;

alter table BDF_R_PAGE
   drop foreign key FK_R_PA_R_B_R_PA2;

alter table BDF_R_PAGE_COMPONENT
   drop foreign key FK_R_PA_R_B_R_CO23;

alter table BDF_R_PAGE_COMPONENT
   drop foreign key FK_R_PA_R_B_R_PA3;

alter table BDF_R_PARAMETER
   drop foreign key FK_R_PA_R_B_R_PA16;

alter table BDF_R_VALIDATOR_PROPERTY
   drop foreign key "F_Reference_35";

alter table BDF_R_ACTION
   drop primary key;

drop table BDF_R_ACTION;

alter table BDF_R_ACTION_DEF
   drop primary key;

drop table BDF_R_ACTION_DEF;

alter table BDF_R_ACTION_DEF_PARAMETER
   drop primary key;

drop table BDF_R_ACTION_DEF_PARAMETER;

alter table BDF_R_ACTION_DEF_RELATION
   drop primary key;

drop table BDF_R_ACTION_DEF_RELATION;

alter table BDF_R_ACTION_PARAMETER
   drop primary key;

drop table BDF_R_ACTION_PARAMETER;

alter table BDF_R_COMPONENT
   drop primary key;

drop table BDF_R_COMPONENT;

alter table BDF_R_COMPONENT_EVENT
   drop primary key;

drop table BDF_R_COMPONENT_EVENT;

alter table BDF_R_COMPONENT_PROPERTY
   drop primary key;

drop table BDF_R_COMPONENT_PROPERTY;

alter table BDF_R_ENTITY
   drop primary key;

drop table BDF_R_ENTITY;

alter table BDF_R_ENTITY_FIELD
   drop primary key;

drop table BDF_R_ENTITY_FIELD;

alter table BDF_R_ENTITY_PARAMETER
   drop primary key;

drop table BDF_R_ENTITY_PARAMETER;

alter table BDF_R_FIELD_METADATA
   drop primary key;

drop table BDF_R_FIELD_METADATA;

alter table BDF_R_LAYOUT_CONSTRAINT_PROP
   drop primary key;

drop table BDF_R_LAYOUT_CONSTRAINT_PROP;

alter table BDF_R_LAYOUT_PROPERTY
   drop primary key;

drop table BDF_R_LAYOUT_PROPERTY;

alter table BDF_R_MAPPING
   drop primary key;

drop table BDF_R_MAPPING;

alter table BDF_R_PACKAGE_INFO
   drop primary key;

drop table BDF_R_PACKAGE_INFO;

alter table BDF_R_PAGE
   drop primary key;

drop table BDF_R_PAGE;

alter table BDF_R_PAGE_COMPONENT
   drop primary key;

drop table BDF_R_PAGE_COMPONENT;

alter table BDF_R_PARAMETER
   drop primary key;

drop table BDF_R_PARAMETER;

alter table BDF_R_VALIDATOR
   drop primary key;

drop table BDF_R_VALIDATOR;

alter table BDF_R_VALIDATOR_PROPERTY
   drop primary key;

drop table BDF_R_VALIDATOR_PROPERTY;

--==============================================================
-- Table: BDF_R_ACTION
--==============================================================
create table BDF_R_ACTION
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(250),
   BEAN_ID_             VARCHAR(100)
);

comment on table BDF_R_ACTION is
'具体动作信息表';

comment on column BDF_R_ACTION.ID_ is
'主键';

comment on column BDF_R_ACTION.NAME_ is
'名称';

comment on column BDF_R_ACTION.BEAN_ID_ is
'定义在Spring当中的BeanID';

alter table BDF_R_ACTION
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_ACTION_DEF
--==============================================================
create table BDF_R_ACTION_DEF
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   DESC_                VARCHAR(50),
   TYPE_                VARCHAR(10),
   SCRIPT_              VARCHAR(1000),
   ENTITY_ID_           VARCHAR(50),
   ASYNC_               CHAR(1),
   CONFIRM_MESSAGE_     VARCHAR(100),
   SUCCESS_MESSAGE_     VARCHAR(100),
   BEFORE_EXECUTE_SCRIPT_ VARCHAR(1000),
   ON_SUCCESS_SCRIPT_   VARCHAR(1000),
   PACKAGE_ID_          VARCHAR(50)
);

comment on table BDF_R_ACTION_DEF is
'目前只支持两种类型的动作：ajaxAction及updateAction，分别对应d7中的两种类型的action';

comment on column BDF_R_ACTION_DEF.ID_ is
'主键';

comment on column BDF_R_ACTION_DEF.NAME_ is
'动作名称';

comment on column BDF_R_ACTION_DEF.DESC_ is
'描述';

comment on column BDF_R_ACTION_DEF.TYPE_ is
'两种类型A(ajax)及U(update)';

comment on column BDF_R_ACTION_DEF.SCRIPT_ is
'动作脚本';

comment on column BDF_R_ACTION_DEF.ENTITY_ID_ is
'动作涉及到的实体对象';

comment on column BDF_R_ACTION_DEF.ASYNC_ is
'是否采用异步执行';

comment on column BDF_R_ACTION_DEF.CONFIRM_MESSAGE_ is
'执行前确认消息';

comment on column BDF_R_ACTION_DEF.SUCCESS_MESSAGE_ is
'执行成功后消息';

comment on column BDF_R_ACTION_DEF.BEFORE_EXECUTE_SCRIPT_ is
'执行动作前的事件脚本';

comment on column BDF_R_ACTION_DEF.ON_SUCCESS_SCRIPT_ is
'执行动作成功后的事件脚本';

comment on column BDF_R_ACTION_DEF.PACKAGE_ID_ is
'所在包';

alter table BDF_R_ACTION_DEF
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_ACTION_DEF_PARAMETER
--==============================================================
create table BDF_R_ACTION_DEF_PARAMETER
(
   ID_                  VARCHAR(50)            not null,
   ACTION_DEF_ID_       VARCHAR(50),
   PARAMETER_ID_        VARCHAR(50)
);

comment on table BDF_R_ACTION_DEF_PARAMETER is
'动作参数关系表';

comment on column BDF_R_ACTION_DEF_PARAMETER.ID_ is
'主键';

comment on column BDF_R_ACTION_DEF_PARAMETER.ACTION_DEF_ID_ is
'所属动作ID';

comment on column BDF_R_ACTION_DEF_PARAMETER.PARAMETER_ID_ is
'所属参数ID';

alter table BDF_R_ACTION_DEF_PARAMETER
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_ACTION_DEF_RELATION
--==============================================================
create table BDF_R_ACTION_DEF_RELATION
(
   ID_                  VARCHAR(50)            not null,
   ACTION_ID_           VARCHAR(50),
   ACTION_DEF_ID_       VARCHAR(50)
);

comment on table BDF_R_ACTION_DEF_RELATION is
'动作定义关系表';

comment on column BDF_R_ACTION_DEF_RELATION.ID_ is
'主键';

comment on column BDF_R_ACTION_DEF_RELATION.ACTION_ID_ is
'动作ID';

comment on column BDF_R_ACTION_DEF_RELATION.ACTION_DEF_ID_ is
'动作定义ID';

alter table BDF_R_ACTION_DEF_RELATION
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_ACTION_PARAMETER
--==============================================================
create table BDF_R_ACTION_PARAMETER
(
   ID_                  VARCHAR(50)            not null,
   ACTION_ID_           VARCHAR(50),
   PARAMETER_ID_        VARCHAR(50)
);

comment on table BDF_R_ACTION_PARAMETER is
'具体动作参数关系表';

comment on column BDF_R_ACTION_PARAMETER.ID_ is
'主键';

comment on column BDF_R_ACTION_PARAMETER.ACTION_ID_ is
'具体动作ID';

comment on column BDF_R_ACTION_PARAMETER.PARAMETER_ID_ is
'参数ID';

alter table BDF_R_ACTION_PARAMETER
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_COMPONENT
--==============================================================
create table BDF_R_COMPONENT
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   DESC_                VARCHAR(50),
   CLASS_NAME_          VARCHAR(250),
   ENTITY_ID_           VARCHAR(50),
   PARENT_ID_           VARCHAR(50),
   LAYOUT_              VARCHAR(20),
   ACTION_DEF_ID_       VARCHAR(50),
   CONTAINER_           CHAR(1),
   PACKAGE_ID_          VARCHAR(50),
   ORDER_               INTEGER
);

comment on table BDF_R_COMPONENT is
'Dorado7组件信息表';

comment on column BDF_R_COMPONENT.ID_ is
'主键';

comment on column BDF_R_COMPONENT.NAME_ is
'组件名称';

comment on column BDF_R_COMPONENT.DESC_ is
'描述';

comment on column BDF_R_COMPONENT.CLASS_NAME_ is
'组件实现类名';

comment on column BDF_R_COMPONENT.ENTITY_ID_ is
'采用的实体对象';

comment on column BDF_R_COMPONENT.PARENT_ID_ is
'通过该属性组件之间嵌套，比如AutoForm与其下的Element；Toolbar与其Toolbutton，Grid与其Column等';

comment on column BDF_R_COMPONENT.LAYOUT_ is
'采用的布局';

comment on column BDF_R_COMPONENT.ACTION_DEF_ID_ is
'采用的动作定义ID';

comment on column BDF_R_COMPONENT.CONTAINER_ is
'1表示为容器型，0表示非容器型，容器型组件下可放其它组件';

comment on column BDF_R_COMPONENT.PACKAGE_ID_ is
'所在包';

comment on column BDF_R_COMPONENT.ORDER_ is
'排序号';

alter table BDF_R_COMPONENT
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_COMPONENT_EVENT
--==============================================================
create table BDF_R_COMPONENT_EVENT
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   DESC_                VARCHAR(50),
   SCRIPT_              VARCHAR(2000),
   COMPONENT_ID_        VARCHAR(50)
);

comment on table BDF_R_COMPONENT_EVENT is
'组件的事件信息表';

comment on column BDF_R_COMPONENT_EVENT.ID_ is
'主键';

comment on column BDF_R_COMPONENT_EVENT.NAME_ is
'事件名称';

comment on column BDF_R_COMPONENT_EVENT.DESC_ is
'描述';

comment on column BDF_R_COMPONENT_EVENT.SCRIPT_ is
'事件脚本内容';

comment on column BDF_R_COMPONENT_EVENT.COMPONENT_ID_ is
'所属组件';

alter table BDF_R_COMPONENT_EVENT
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_COMPONENT_PROPERTY
--==============================================================
create table BDF_R_COMPONENT_PROPERTY
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(100),
   VALUE_               VARCHAR(250),
   COMPONENT_ID_        VARCHAR(50)
);

comment on table BDF_R_COMPONENT_PROPERTY is
'组件的属性信息';

comment on column BDF_R_COMPONENT_PROPERTY.ID_ is
'主键';

comment on column BDF_R_COMPONENT_PROPERTY.NAME_ is
'属性名称';

comment on column BDF_R_COMPONENT_PROPERTY.VALUE_ is
'属性值';

comment on column BDF_R_COMPONENT_PROPERTY.COMPONENT_ID_ is
'隶属组件';

alter table BDF_R_COMPONENT_PROPERTY
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_ENTITY
--==============================================================
create table BDF_R_ENTITY
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   TABLE_NAME_          VARCHAR(100),
   RECURSIVE_           CHAR(1),
   DESC_                VARCHAR(50),
   QUERY_SQL_           VARCHAR(1000),
   PACKAGE_ID_          VARCHAR(50),
   PAGE_SIZE_           INTEGER,
   PARENT_ID_           VARCHAR(50)
);

comment on table BDF_R_ENTITY is
'对应数据库中定义的表或视图';

comment on column BDF_R_ENTITY.ID_ is
'主键';

comment on column BDF_R_ENTITY.NAME_ is
'实体名称';

comment on column BDF_R_ENTITY.TABLE_NAME_ is
'表示该实体对象要操作的主表名称';

comment on column BDF_R_ENTITY.RECURSIVE_ is
'是否为递归结构';

comment on column BDF_R_ENTITY.DESC_ is
'描述';

comment on column BDF_R_ENTITY.QUERY_SQL_ is
'查询用的SQL';

comment on column BDF_R_ENTITY.PACKAGE_ID_ is
'所在包';

comment on column BDF_R_ENTITY.PAGE_SIZE_ is
'每页显示记录数';

comment on column BDF_R_ENTITY.PARENT_ID_ is
'隶属实体对象ID';

alter table BDF_R_ENTITY
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_ENTITY_FIELD
--==============================================================
create table BDF_R_ENTITY_FIELD
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(100),
   READ_ONLY_           CHAR(1),
   SUBMITTABLE_         CHAR(1),
   ENTITY_ID_           VARCHAR(50),
   DESC_                VARCHAR(50),
   METADATA_ID_         VARCHAR(50),
   TABLE_NAME_          VARCHAR(100),
   PRIMARY_KEY_         CHAR(1),
   KEY_GENERATE_TYPE_   VARCHAR(20),
   KEY_GENERATOR_       VARCHAR(100),
   MAPPING_ID_          VARCHAR(50),
   DATA_TYPE_           VARCHAR(50),
   LABEL_               VARCHAR(50),
   REQUIRED_            CHAR(1),
   DEFAULT_VALUE_       VARCHAR(50),
   DISPLAY_FORMAT_      VARCHAR(50)
);

comment on table BDF_R_ENTITY_FIELD is
'实体字段信息表';

comment on column BDF_R_ENTITY_FIELD.ID_ is
'主键';

comment on column BDF_R_ENTITY_FIELD.NAME_ is
'字段名';

comment on column BDF_R_ENTITY_FIELD.READ_ONLY_ is
'0表示只读，1表示非只读';

comment on column BDF_R_ENTITY_FIELD.SUBMITTABLE_ is
'是否要提交数据到后台';

comment on column BDF_R_ENTITY_FIELD.ENTITY_ID_ is
'所属实体对象';

comment on column BDF_R_ENTITY_FIELD.DESC_ is
'描述';

comment on column BDF_R_ENTITY_FIELD.METADATA_ID_ is
'采用的元数据对象';

comment on column BDF_R_ENTITY_FIELD.TABLE_NAME_ is
'隶属表';

comment on column BDF_R_ENTITY_FIELD.PRIMARY_KEY_ is
'是否为主键';

comment on column BDF_R_ENTITY_FIELD.KEY_GENERATE_TYPE_ is
'生成方式有:custom、autoincrement、sequence';

comment on column BDF_R_ENTITY_FIELD.KEY_GENERATOR_ is
'可能是具体的EL表达式或一个具体的sequence对象';

comment on column BDF_R_ENTITY_FIELD.MAPPING_ID_ is
'采用的Mapping';

comment on column BDF_R_ENTITY_FIELD.DATA_TYPE_ is
'数据类型';

comment on column BDF_R_ENTITY_FIELD.LABEL_ is
'字段标题';

comment on column BDF_R_ENTITY_FIELD.REQUIRED_ is
'是否为必须';

comment on column BDF_R_ENTITY_FIELD.DEFAULT_VALUE_ is
'默认值';

comment on column BDF_R_ENTITY_FIELD.DISPLAY_FORMAT_ is
'显示格式';

alter table BDF_R_ENTITY_FIELD
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_ENTITY_PARAMETER
--==============================================================
create table BDF_R_ENTITY_PARAMETER
(
   ID_                  VARCHAR(50)            not null,
   ENTITY_ID_           VARCHAR(50),
   PARAMETER_ID_        VARCHAR(50)
);

comment on table BDF_R_ENTITY_PARAMETER is
'实体对象查询条件参数表';

comment on column BDF_R_ENTITY_PARAMETER.ID_ is
'主键';

comment on column BDF_R_ENTITY_PARAMETER.ENTITY_ID_ is
'隶属实体对象ID';

comment on column BDF_R_ENTITY_PARAMETER.PARAMETER_ID_ is
'参数ID';

alter table BDF_R_ENTITY_PARAMETER
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_FIELD_METADATA
--==============================================================
create table BDF_R_FIELD_METADATA
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(100),
   DESC_                VARCHAR(50),
   DEFAULT_VALUE_       VARCHAR(50),
   DISPLAY_FORMAT_      VARCHAR(50),
   REQUIRED_            CHAR(1),
   LABEL_               VARCHAR(100),
   PACKAGE_ID_          VARCHAR(50),
   MAPPING_             VARCHAR(50)
);

comment on table BDF_R_FIELD_METADATA is
'字段元数据信息表';

comment on column BDF_R_FIELD_METADATA.ID_ is
'主键';

comment on column BDF_R_FIELD_METADATA.NAME_ is
'字段名';

comment on column BDF_R_FIELD_METADATA.DESC_ is
'描述';

comment on column BDF_R_FIELD_METADATA.DEFAULT_VALUE_ is
'默认值';

comment on column BDF_R_FIELD_METADATA.DISPLAY_FORMAT_ is
'显示格式';

comment on column BDF_R_FIELD_METADATA.REQUIRED_ is
'是否为必须';

comment on column BDF_R_FIELD_METADATA.LABEL_ is
'字段标题';

comment on column BDF_R_FIELD_METADATA.PACKAGE_ID_ is
'所在包';

comment on column BDF_R_FIELD_METADATA.MAPPING_ is
'数据映射';

alter table BDF_R_FIELD_METADATA
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_LAYOUT_CONSTRAINT_PROP
--==============================================================
create table BDF_R_LAYOUT_CONSTRAINT_PROP
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   VALUE_               VARCHAR(20),
   DESC_                VARCHAR(50),
   COMPONENT_ID_        VARCHAR(50)
);

comment on table BDF_R_LAYOUT_CONSTRAINT_PROP is
'组件约束属性信息表';

comment on column BDF_R_LAYOUT_CONSTRAINT_PROP.ID_ is
'主键';

comment on column BDF_R_LAYOUT_CONSTRAINT_PROP.NAME_ is
'约束属性名称';

comment on column BDF_R_LAYOUT_CONSTRAINT_PROP.VALUE_ is
'约束属性值';

comment on column BDF_R_LAYOUT_CONSTRAINT_PROP.DESC_ is
'描述';

comment on column BDF_R_LAYOUT_CONSTRAINT_PROP.COMPONENT_ID_ is
'该字段中存储的是BDF_R_COMPONENT表主键值或BDF_R_PAGE_COMPONENT表主键值或BDF_RG_PAGE_COMPONENT表主键值';

alter table BDF_R_LAYOUT_CONSTRAINT_PROP
   add constraint PK_B_R_LT_CONS_P1 primary key (ID_);

--==============================================================
-- Table: BDF_R_LAYOUT_PROPERTY
--==============================================================
create table BDF_R_LAYOUT_PROPERTY
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   VALUE_               VARCHAR(20),
   DESC_                VARCHAR(50),
   COMPONENT_ID_        VARCHAR(50)
);

comment on table BDF_R_LAYOUT_PROPERTY is
'组件布局属性信息表';

comment on column BDF_R_LAYOUT_PROPERTY.ID_ is
'主键';

comment on column BDF_R_LAYOUT_PROPERTY.NAME_ is
'属性名称';

comment on column BDF_R_LAYOUT_PROPERTY.VALUE_ is
'属性值';

comment on column BDF_R_LAYOUT_PROPERTY.DESC_ is
'描述';

comment on column BDF_R_LAYOUT_PROPERTY.COMPONENT_ID_ is
'该字段中存储的是BDF_R_COMPONENT表主键值或BDF_R_PAGE_COMPONENT表主键值或BDF_RG_PAGE_COMPONENT表主键值';

alter table BDF_R_LAYOUT_PROPERTY
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_MAPPING
--==============================================================
create table BDF_R_MAPPING
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   SOURCE_              VARCHAR(10),
   VALUE_FIELD_         VARCHAR(100),
   KEY_FIELD_           VARCHAR(100),
   QUERY_SQL_           VARCHAR(250),
   CUSTOM_KEY_VALUE_    VARCHAR(250),
   PACKAGE_ID_          VARCHAR(50),
   PROPERTY_            VARCHAR(100)
);

comment on table BDF_R_MAPPING is
'值映射信息表';

comment on column BDF_R_MAPPING.ID_ is
'主键';

comment on column BDF_R_MAPPING.NAME_ is
'名称';

comment on column BDF_R_MAPPING.SOURCE_ is
'custom表示用户自定义；table表示数据库中的表';

comment on column BDF_R_MAPPING.VALUE_FIELD_ is
'用于显示的字段名';

comment on column BDF_R_MAPPING.KEY_FIELD_ is
'用于实际值的字段名';

comment on column BDF_R_MAPPING.QUERY_SQL_ is
'查询表中键值所用SQL';

comment on column BDF_R_MAPPING.CUSTOM_KEY_VALUE_ is
'格式为：key1=value1;key2=value2';

comment on column BDF_R_MAPPING.PACKAGE_ID_ is
'所在包';

comment on column BDF_R_MAPPING.PROPERTY_ is
'下拉框值回填属性名';

alter table BDF_R_MAPPING
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_PACKAGE_INFO
--==============================================================
create table BDF_R_PACKAGE_INFO
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   PARENT_ID_           VARCHAR(50),
   TYPE_                VARCHAR(10),
   DESC_                VARCHAR(50)
);

comment on table BDF_R_PACKAGE_INFO is
'包信息表';

comment on column BDF_R_PACKAGE_INFO.ID_ is
'主键';

comment on column BDF_R_PACKAGE_INFO.NAME_ is
'目录名称';

comment on column BDF_R_PACKAGE_INFO.PARENT_ID_ is
'上级目录ID';

comment on column BDF_R_PACKAGE_INFO.TYPE_ is
'page用于存放主页面；subpage用于存放子页面；component用于存放组件；action用于存放动作;entity用于存储实体;parameter用于存储参数;metadata用于存储元数据';

comment on column BDF_R_PACKAGE_INFO.DESC_ is
'描述用于显示';

alter table BDF_R_PACKAGE_INFO
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_PAGE
--==============================================================
create table BDF_R_PAGE
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(100),
   PACKAGE_ID_          VARCHAR(50),
   LAYOUT_              VARCHAR(20),
   DESC_                VARCHAR(50)
);

comment on table BDF_R_PAGE is
'主页面信息表';

comment on column BDF_R_PAGE.ID_ is
'主键';

comment on column BDF_R_PAGE.NAME_ is
'页面名称';

comment on column BDF_R_PAGE.PACKAGE_ID_ is
'所在包';

comment on column BDF_R_PAGE.LAYOUT_ is
'采用的布局';

comment on column BDF_R_PAGE.DESC_ is
'描述';

alter table BDF_R_PAGE
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_PAGE_COMPONENT
--==============================================================
create table BDF_R_PAGE_COMPONENT
(
   ID_                  VARCHAR(50)            not null,
   PAGE_ID_             VARCHAR(50),
   ORDER_               INTEGER,
   COMPONENT_ID_        VARCHAR(50),
   READ_ONLY_           CHAR(1)
);

comment on table BDF_R_PAGE_COMPONENT is
'主页面的组件集信息表';

comment on column BDF_R_PAGE_COMPONENT.ID_ is
'主键';

comment on column BDF_R_PAGE_COMPONENT.PAGE_ID_ is
'所属主页面';

comment on column BDF_R_PAGE_COMPONENT.ORDER_ is
'排序号';

comment on column BDF_R_PAGE_COMPONENT.COMPONENT_ID_ is
'对应的组件ID';

comment on column BDF_R_PAGE_COMPONENT.READ_ONLY_ is
'如果为只读，那么该组件及其下所有组件生成时都自动添加一个readOnly属性';

alter table BDF_R_PAGE_COMPONENT
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_PARAMETER
--==============================================================
create table BDF_R_PARAMETER
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   DESC_                VARCHAR(50),
   VALUE_               VARCHAR(100),
   TYPE_                VARCHAR(10),
   PACKAGE_ID_          VARCHAR(50)
);

comment on table BDF_R_PARAMETER is
'查询更新参数信息表';

comment on column BDF_R_PARAMETER.ID_ is
'主键';

comment on column BDF_R_PARAMETER.NAME_ is
'参数名';

comment on column BDF_R_PARAMETER.DESC_ is
'描述';

comment on column BDF_R_PARAMETER.VALUE_ is
'参数值可以是一个固定的值，也可以是一个EL表达式，如${abc},或者以#{开头，表示一个BSH表达式；或者为空，为空表示加载数据时从前台传入的参数中取';

comment on column BDF_R_PARAMETER.TYPE_ is
'query表示查询，update表示更新，insert表示新增';

comment on column BDF_R_PARAMETER.PACKAGE_ID_ is
'所在包';

alter table BDF_R_PARAMETER
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_VALIDATOR
--==============================================================
create table BDF_R_VALIDATOR
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   DESC_                VARCHAR(50),
   FIELD_ID_            VARCHAR(50)
);

comment on table BDF_R_VALIDATOR is
'字段验证器信息表';

comment on column BDF_R_VALIDATOR.ID_ is
'主键';

comment on column BDF_R_VALIDATOR.NAME_ is
'验证器名称';

comment on column BDF_R_VALIDATOR.DESC_ is
'描述';

comment on column BDF_R_VALIDATOR.FIELD_ID_ is
'可能是Mapping表的ID，也可能是实体字段表的ID';

alter table BDF_R_VALIDATOR
   add constraint "P_Key_1" primary key (ID_);

--==============================================================
-- Table: BDF_R_VALIDATOR_PROPERTY
--==============================================================
create table BDF_R_VALIDATOR_PROPERTY
(
   ID_                  VARCHAR(50)            not null,
   NAME_                VARCHAR(50),
   VALUE_               VARCHAR(200),
   VALIDATOR_ID_        VARCHAR(50)
);

comment on table BDF_R_VALIDATOR_PROPERTY is
'验证器属性信息表';

comment on column BDF_R_VALIDATOR_PROPERTY.ID_ is
'主键';

comment on column BDF_R_VALIDATOR_PROPERTY.NAME_ is
'属性名';

comment on column BDF_R_VALIDATOR_PROPERTY.VALUE_ is
'属性值';

comment on column BDF_R_VALIDATOR_PROPERTY.VALIDATOR_ID_ is
'隶属验证器';

alter table BDF_R_VALIDATOR_PROPERTY
   add constraint "P_Key_1" primary key (ID_);

alter table BDF_R_ACTION_DEF
   add constraint FK_R_PA_R_B_R_EN11 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF
   add constraint FK_R_PA_R_B_R_PA12 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF_PARAMETER
   add constraint FK_R_PA_R_B_R_AC24 foreign key (ACTION_DEF_ID_)
      references BDF_R_ACTION_DEF (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF_PARAMETER
   add constraint FK_R_PA_R_B_R_PA25 foreign key (PARAMETER_ID_)
      references BDF_R_PARAMETER (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF_RELATION
   add constraint FK_R_PA_R_B_R_ACT foreign key (ACTION_ID_)
      references BDF_R_ACTION (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF_RELATION
   add constraint FK_R_PA_R_B_R_DA1 foreign key (ACTION_DEF_ID_)
      references BDF_R_ACTION_DEF (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ACTION_PARAMETER
   add constraint "F_Reference_36" foreign key (ACTION_ID_)
      references BDF_R_ACTION (ID_)
      on delete no action on update no action;

alter table BDF_R_ACTION_PARAMETER
   add constraint "F_Reference_37" foreign key (PARAMETER_ID_)
      references BDF_R_PARAMETER (ID_)
      on delete no action on update no action;

alter table BDF_R_COMPONENT
   add constraint FK_R_PA_R_B_R_CO9 foreign key (PARENT_ID_)
      references BDF_R_COMPONENT (ID_)
      on delete restrict on update restrict;

alter table BDF_R_COMPONENT
   add constraint FK_R_PA_R_B_R_PA13 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
      on delete restrict on update restrict;

alter table BDF_R_COMPONENT
   add constraint "F_Reference_31" foreign key (ACTION_DEF_ID_)
      references BDF_R_ACTION_DEF (ID_)
      on delete restrict on update restrict;

alter table BDF_R_COMPONENT
   add constraint FK_R_PA_R_B_R_EN7 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_)
      on delete restrict on update restrict;

alter table BDF_R_COMPONENT_EVENT
   add constraint FK_R_PA_R_B_R_CO5 foreign key (COMPONENT_ID_)
      references BDF_R_COMPONENT (ID_)
      on delete restrict on update restrict;

alter table BDF_R_COMPONENT_PROPERTY
   add constraint FK_R_PA_R_B_R_CO4 foreign key (COMPONENT_ID_)
      references BDF_R_COMPONENT (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ENTITY
   add constraint FK_R_PA_R_B_R_PA14 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ENTITY
   add constraint FK_R_PA_R_B_R_EN18 foreign key (PARENT_ID_)
      references BDF_R_ENTITY (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ENTITY_FIELD
   add constraint FK_R_PA_R_B_R_FI8 foreign key (METADATA_ID_)
      references BDF_R_FIELD_METADATA (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ENTITY_FIELD
   add constraint "F_Reference_34" foreign key (MAPPING_ID_)
      references BDF_R_MAPPING (ID_)
      on delete no action on update no action;

alter table BDF_R_ENTITY_FIELD
   add constraint FK_R_PA_R_B_R_EN6 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ENTITY_PARAMETER
   add constraint FK_R_PA_R_B_R_EN19 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_)
      on delete restrict on update restrict;

alter table BDF_R_ENTITY_PARAMETER
   add constraint FK_R_PA_R_B_R_PA21 foreign key (PARAMETER_ID_)
      references BDF_R_PARAMETER (ID_)
      on delete restrict on update restrict;

alter table BDF_R_FIELD_METADATA
   add constraint FK_R_PA_R_B_R_PA15 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
      on delete restrict on update restrict;

alter table BDF_R_FIELD_METADATA
   add constraint FK_R_PA_R_B_R_ME17 foreign key (MAPPING_)
      references BDF_R_MAPPING (ID_)
      on delete restrict on update restrict;

alter table BDF_R_MAPPING
   add constraint FK_R_PA_R_B_R_PA22 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
      on delete restrict on update restrict;

alter table BDF_R_PACKAGE_INFO
   add constraint FK_R_PA_R_B_R_PA1 foreign key (PARENT_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
      on delete restrict on update restrict;

alter table BDF_R_PAGE
   add constraint FK_R_PA_R_B_R_PA2 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
      on delete restrict on update restrict;

alter table BDF_R_PAGE_COMPONENT
   add constraint FK_R_PA_R_B_R_CO23 foreign key (COMPONENT_ID_)
      references BDF_R_COMPONENT (ID_)
      on delete restrict on update restrict;

alter table BDF_R_PAGE_COMPONENT
   add constraint FK_R_PA_R_B_R_PA3 foreign key (PAGE_ID_)
      references BDF_R_PAGE (ID_)
      on delete restrict on update restrict;

alter table BDF_R_PARAMETER
   add constraint FK_R_PA_R_B_R_PA16 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
      on delete restrict on update restrict;

alter table BDF_R_VALIDATOR_PROPERTY
   add constraint "F_Reference_35" foreign key (VALIDATOR_ID_)
      references BDF_R_VALIDATOR (ID_)
      on delete no action on update no action;

