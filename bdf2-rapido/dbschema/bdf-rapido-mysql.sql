/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2012/8/24 16:23:33                           */
/*==============================================================*/


alter table BDF_R_ACTION
   drop primary key;

drop table if exists BDF_R_ACTION;

alter table BDF_R_ACTION_DEF
   drop primary key;

drop table if exists BDF_R_ACTION_DEF;

alter table BDF_R_ACTION_DEF_PARAMETER
   drop primary key;

drop table if exists BDF_R_ACTION_DEF_PARAMETER;

alter table BDF_R_ACTION_DEF_RELATION
   drop primary key;

drop table if exists BDF_R_ACTION_DEF_RELATION;

alter table BDF_R_ACTION_PARAMETER
   drop primary key;

drop table if exists BDF_R_ACTION_PARAMETER;

alter table BDF_R_COMPONENT
   drop primary key;

drop table if exists BDF_R_COMPONENT;

alter table BDF_R_COMPONENT_EVENT
   drop primary key;

drop table if exists BDF_R_COMPONENT_EVENT;

alter table BDF_R_COMPONENT_PROPERTY
   drop primary key;

drop table if exists BDF_R_COMPONENT_PROPERTY;

alter table BDF_R_ENTITY
   drop primary key;

drop table if exists BDF_R_ENTITY;

alter table BDF_R_ENTITY_FIELD
   drop primary key;

drop table if exists BDF_R_ENTITY_FIELD;

alter table BDF_R_ENTITY_PARAMETER
   drop primary key;

drop table if exists BDF_R_ENTITY_PARAMETER;

alter table BDF_R_FIELD_METADATA
   drop primary key;

drop table if exists BDF_R_FIELD_METADATA;

alter table BDF_R_LAYOUT_CONSTRAINT_PROP
   drop primary key;

drop table if exists BDF_R_LAYOUT_CONSTRAINT_PROP;

alter table BDF_R_LAYOUT_PROPERTY
   drop primary key;

drop table if exists BDF_R_LAYOUT_PROPERTY;

alter table BDF_R_MAPPING
   drop primary key;

drop table if exists BDF_R_MAPPING;

alter table BDF_R_PACKAGE_INFO
   drop primary key;

drop table if exists BDF_R_PACKAGE_INFO;

alter table BDF_R_PAGE
   drop primary key;

drop table if exists BDF_R_PAGE;

alter table BDF_R_PAGE_COMPONENT
   drop primary key;

drop table if exists BDF_R_PAGE_COMPONENT;

alter table BDF_R_PARAMETER
   drop primary key;

drop table if exists BDF_R_PARAMETER;

alter table BDF_R_VALIDATOR
   drop primary key;

drop table if exists BDF_R_VALIDATOR;

alter table BDF_R_VALIDATOR_PROPERTY
   drop primary key;

drop table if exists BDF_R_VALIDATOR_PROPERTY;

/*==============================================================*/
/* Table: BDF_R_ACTION                                          */
/*==============================================================*/
create table BDF_R_ACTION
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(250) comment '名称',
   BEAN_ID_             varchar(100) comment '定义在Spring当中的BeanID'
);

alter table BDF_R_ACTION comment '具体动作信息表';

alter table BDF_R_ACTION
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_ACTION_DEF                                      */
/*==============================================================*/
create table BDF_R_ACTION_DEF
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '动作名称',
   DESC_                varchar(50) comment '描述',
   TYPE_                varchar(10) comment '两种类型A(ajax)及U(update)',
   SCRIPT_              varchar(1000) comment '动作脚本',
   ENTITY_ID_           varchar(50) comment '动作涉及到的实体对象',
   ASYNC_               char(1) comment '是否采用异步执行',
   CONFIRM_MESSAGE_     varchar(100) comment '执行前确认消息',
   SUCCESS_MESSAGE_     varchar(100) comment '执行成功后消息',
   BEFORE_EXECUTE_SCRIPT_ varchar(1000) comment '执行动作前的事件脚本',
   ON_SUCCESS_SCRIPT_   varchar(1000) comment '执行动作成功后的事件脚本',
   PACKAGE_ID_          varchar(50) comment '所在包'
);

alter table BDF_R_ACTION_DEF comment '目前只支持两种类型的动作：ajaxAction及updateAction，分别对应d7中的两种类型的action';

alter table BDF_R_ACTION_DEF
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_ACTION_DEF_PARAMETER                            */
/*==============================================================*/
create table BDF_R_ACTION_DEF_PARAMETER
(
   ID_                  varchar(50) not null comment '主键',
   ACTION_DEF_ID_       varchar(50) comment '所属动作ID',
   PARAMETER_ID_        varchar(50) comment '所属参数ID'
);

alter table BDF_R_ACTION_DEF_PARAMETER comment '动作参数关系表';

alter table BDF_R_ACTION_DEF_PARAMETER
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_ACTION_DEF_RELATION                             */
/*==============================================================*/
create table BDF_R_ACTION_DEF_RELATION
(
   ID_                  varchar(50) not null comment '主键',
   ACTION_ID_           varchar(50) comment '动作ID',
   ACTION_DEF_ID_       varchar(50) comment '动作定义ID'
);

alter table BDF_R_ACTION_DEF_RELATION comment '动作定义关系表';

alter table BDF_R_ACTION_DEF_RELATION
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_ACTION_PARAMETER                                */
/*==============================================================*/
create table BDF_R_ACTION_PARAMETER
(
   ID_                  varchar(50) not null comment '主键',
   ACTION_ID_           varchar(50) comment '具体动作ID',
   PARAMETER_ID_        varchar(50) comment '参数ID'
);

alter table BDF_R_ACTION_PARAMETER comment '具体动作参数关系表';

alter table BDF_R_ACTION_PARAMETER
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_COMPONENT                                       */
/*==============================================================*/
create table BDF_R_COMPONENT
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '组件名称',
   DESC_                varchar(50) comment '描述',
   CLASS_NAME_          varchar(250) comment '组件实现类名',
   ENTITY_ID_           varchar(50) comment '采用的实体对象',
   PARENT_ID_           varchar(50) comment '通过该属性组件之间嵌套，比如AutoForm与其下的Element；Toolbar与其Toolbutton，Grid与其Column等',
   LAYOUT_              varchar(20) comment '采用的布局',
   ACTION_DEF_ID_       varchar(50) comment '采用的动作定义ID',
   CONTAINER_           char(1) comment '1表示为容器型，0表示非容器型，容器型组件下可放其它组件',
   PACKAGE_ID_          varchar(50) comment '所在包',
   ORDER_               int comment '排序号'
);

alter table BDF_R_COMPONENT comment 'Dorado7组件信息表';

alter table BDF_R_COMPONENT
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_COMPONENT_EVENT                                 */
/*==============================================================*/
create table BDF_R_COMPONENT_EVENT
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '事件名称',
   DESC_                varchar(50) comment '描述',
   SCRIPT_              varchar(2000) comment '事件脚本内容',
   COMPONENT_ID_        varchar(50) comment '所属组件'
);

alter table BDF_R_COMPONENT_EVENT comment '组件的事件信息表';

alter table BDF_R_COMPONENT_EVENT
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_COMPONENT_PROPERTY                              */
/*==============================================================*/
create table BDF_R_COMPONENT_PROPERTY
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(100) comment '属性名称',
   VALUE_               varchar(250) comment '属性值',
   COMPONENT_ID_        varchar(50) comment '隶属组件'
);

alter table BDF_R_COMPONENT_PROPERTY comment '组件的属性信息';

alter table BDF_R_COMPONENT_PROPERTY
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_ENTITY                                          */
/*==============================================================*/
create table BDF_R_ENTITY
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '实体名称',
   TABLE_NAME_          varchar(100) comment '表示该实体对象要操作的主表名称',
   RECURSIVE_           char(1) comment '是否为递归结构',
   DESC_                varchar(50) comment '描述',
   QUERY_SQL_           varchar(1000) comment '查询用的SQL',
   PACKAGE_ID_          varchar(50) comment '所在包',
   PAGE_SIZE_           int comment '每页显示记录数',
   PARENT_ID_           varchar(50) comment '隶属实体对象ID'
);

alter table BDF_R_ENTITY comment '对应数据库中定义的表或视图';

alter table BDF_R_ENTITY
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_ENTITY_FIELD                                    */
/*==============================================================*/
create table BDF_R_ENTITY_FIELD
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(100) comment '字段名',
   READ_ONLY_           char(1) comment '0表示只读，1表示非只读',
   SUBMITTABLE_         char(1) comment '是否要提交数据到后台',
   ENTITY_ID_           varchar(50) comment '所属实体对象',
   DESC_                varchar(50) comment '描述',
   METADATA_ID_         varchar(50) comment '采用的元数据对象',
   TABLE_NAME_          varchar(100) comment '隶属表',
   PRIMARY_KEY_         char(1) comment '是否为主键',
   KEY_GENERATE_TYPE_   varchar(20) comment '生成方式有:custom、autoincrement、sequence',
   KEY_GENERATOR_       varchar(100) comment '可能是具体的EL表达式或一个具体的sequence对象',
   MAPPING_ID_          varchar(50) comment '采用的Mapping',
   DATA_TYPE_           varchar(50) comment '数据类型',
   LABEL_               varchar(50) comment '字段标题',
   REQUIRED_            char(1) comment '是否为必须',
   DEFAULT_VALUE_       varchar(50) comment '默认值',
   DISPLAY_FORMAT_      varchar(50) comment '显示格式'
);

alter table BDF_R_ENTITY_FIELD comment '实体字段信息表';

alter table BDF_R_ENTITY_FIELD
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_ENTITY_PARAMETER                                */
/*==============================================================*/
create table BDF_R_ENTITY_PARAMETER
(
   ID_                  varchar(50) not null comment '主键',
   ENTITY_ID_           varchar(50) comment '隶属实体对象ID',
   PARAMETER_ID_        varchar(50) comment '参数ID'
);

alter table BDF_R_ENTITY_PARAMETER comment '实体对象查询条件参数表';

alter table BDF_R_ENTITY_PARAMETER
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_FIELD_METADATA                                  */
/*==============================================================*/
create table BDF_R_FIELD_METADATA
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(100) comment '字段名',
   DESC_                varchar(50) comment '描述',
   DEFAULT_VALUE_       varchar(50) comment '默认值',
   DISPLAY_FORMAT_      varchar(50) comment '显示格式',
   REQUIRED_            char(1) comment '是否为必须',
   LABEL_               varchar(100) comment '字段标题',
   PACKAGE_ID_          varchar(50) comment '所在包',
   MAPPING_             varchar(50) comment '数据映射'
);

alter table BDF_R_FIELD_METADATA comment '字段元数据信息表';

alter table BDF_R_FIELD_METADATA
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_LAYOUT_CONSTRAINT_PROP                          */
/*==============================================================*/
create table BDF_R_LAYOUT_CONSTRAINT_PROP
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '约束属性名称',
   VALUE_               varchar(20) comment '约束属性值',
   DESC_                varchar(50) comment '描述',
   COMPONENT_ID_        varchar(50) comment '该字段中存储的是BDF_R_COMPONENT表主键值或BDF_R_PAGE_COMPONENT表主键值或BDF_RG_PAGE_COMPONENT表主键值'
);

alter table BDF_R_LAYOUT_CONSTRAINT_PROP comment '组件约束属性信息表';

alter table BDF_R_LAYOUT_CONSTRAINT_PROP
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_LAYOUT_PROPERTY                                 */
/*==============================================================*/
create table BDF_R_LAYOUT_PROPERTY
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '属性名称',
   VALUE_               varchar(20) comment '属性值',
   DESC_                varchar(50) comment '描述',
   COMPONENT_ID_        varchar(50) comment '该字段中存储的是BDF_R_COMPONENT表主键值或BDF_R_PAGE_COMPONENT表主键值或BDF_RG_PAGE_COMPONENT表主键值'
);

alter table BDF_R_LAYOUT_PROPERTY comment '组件布局属性信息表';

alter table BDF_R_LAYOUT_PROPERTY
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_MAPPING                                         */
/*==============================================================*/
create table BDF_R_MAPPING
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '名称',
   SOURCE_              varchar(10) comment 'custom表示用户自定义；table表示数据库中的表',
   VALUE_FIELD_         varchar(100) comment '用于显示的字段名',
   KEY_FIELD_           varchar(100) comment '用于实际值的字段名',
   QUERY_SQL_           varchar(250) comment '查询表中键值所用SQL',
   CUSTOM_KEY_VALUE_    varchar(250) comment '格式为：key1=value1;key2=value2',
   PACKAGE_ID_          varchar(50) comment '所在包',
   PROPERTY_            varchar(100) comment '下拉框值回填属性名'
);

alter table BDF_R_MAPPING comment '值映射信息表';

alter table BDF_R_MAPPING
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_PACKAGE_INFO                                    */
/*==============================================================*/
create table BDF_R_PACKAGE_INFO
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '目录名称',
   PARENT_ID_           varchar(50) comment '上级目录ID',
   TYPE_                varchar(10) comment 'page用于存放主页面；subpage用于存放子页面；component用于存放组件；action用于存放动作;entity用于存储实体;parameter用于存储参数;metadata用于存储元数据',
   DESC_                varchar(50) comment '描述用于显示'
);

alter table BDF_R_PACKAGE_INFO comment '包信息表';

alter table BDF_R_PACKAGE_INFO
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_PAGE                                            */
/*==============================================================*/
create table BDF_R_PAGE
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(100) comment '页面名称',
   PACKAGE_ID_          varchar(50) comment '所在包',
   LAYOUT_              varchar(20) comment '采用的布局',
   DESC_                varchar(50) comment '描述'
);

alter table BDF_R_PAGE comment '主页面信息表';

alter table BDF_R_PAGE
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_PAGE_COMPONENT                                  */
/*==============================================================*/
create table BDF_R_PAGE_COMPONENT
(
   ID_                  varchar(50) not null comment '主键',
   PAGE_ID_             varchar(50) comment '所属主页面',
   ORDER_               int comment '排序号',
   COMPONENT_ID_        varchar(50) comment '对应的组件ID',
   READ_ONLY_           char(1) comment '如果为只读，那么该组件及其下所有组件生成时都自动添加一个readOnly属性'
);

alter table BDF_R_PAGE_COMPONENT comment '主页面的组件集信息表';

alter table BDF_R_PAGE_COMPONENT
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_PARAMETER                                       */
/*==============================================================*/
create table BDF_R_PARAMETER
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '参数名',
   DESC_                varchar(50) comment '描述',
   VALUE_               varchar(100) comment '参数值可以是一个固定的值，也可以是一个EL表达式，如${abc},或者以#{开头，表示一个BSH表达式；或者为空，为空表示加载数据时从前台传入的参数中取',
   TYPE_                varchar(10) comment 'query表示查询，update表示更新，insert表示新增',
   PACKAGE_ID_          varchar(50) comment '所在包'
);

alter table BDF_R_PARAMETER comment '查询更新参数信息表';

alter table BDF_R_PARAMETER
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_VALIDATOR                                       */
/*==============================================================*/
create table BDF_R_VALIDATOR
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '验证器名称',
   DESC_                varchar(50) comment '描述',
   FIELD_ID_            varchar(50) comment '可能是Mapping表的ID，也可能是实体字段表的ID'
);

alter table BDF_R_VALIDATOR comment '字段验证器信息表';

alter table BDF_R_VALIDATOR
   add primary key (ID_);

/*==============================================================*/
/* Table: BDF_R_VALIDATOR_PROPERTY                              */
/*==============================================================*/
create table BDF_R_VALIDATOR_PROPERTY
(
   ID_                  varchar(50) not null comment '主键',
   NAME_                varchar(50) comment '属性名',
   VALUE_               varchar(200) comment '属性值',
   VALIDATOR_ID_        varchar(50) comment '隶属验证器'
);

alter table BDF_R_VALIDATOR_PROPERTY comment '验证器属性信息表';

alter table BDF_R_VALIDATOR_PROPERTY
   add primary key (ID_);

alter table BDF_R_ACTION_DEF add constraint FK_R_PA_R_B_R_EN11 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_) on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF add constraint FK_R_PA_R_B_R_PA12 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_) on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF_PARAMETER add constraint FK_R_PA_R_B_R_AC24 foreign key (ACTION_DEF_ID_)
      references BDF_R_ACTION_DEF (ID_) on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF_PARAMETER add constraint FK_R_PA_R_B_R_PA25 foreign key (PARAMETER_ID_)
      references BDF_R_PARAMETER (ID_) on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF_RELATION add constraint FK_R_PA_R_B_R_ACT foreign key (ACTION_ID_)
      references BDF_R_ACTION (ID_) on delete restrict on update restrict;

alter table BDF_R_ACTION_DEF_RELATION add constraint FK_R_PA_R_B_R_DA1 foreign key (ACTION_DEF_ID_)
      references BDF_R_ACTION_DEF (ID_) on delete restrict on update restrict;

alter table BDF_R_ACTION_PARAMETER add constraint FK_Reference_36 foreign key (ACTION_ID_)
      references BDF_R_ACTION (ID_);

alter table BDF_R_ACTION_PARAMETER add constraint FK_Reference_37 foreign key (PARAMETER_ID_)
      references BDF_R_PARAMETER (ID_);

alter table BDF_R_COMPONENT add constraint FK_R_PA_R_B_R_CO9 foreign key (PARENT_ID_)
      references BDF_R_COMPONENT (ID_) on delete restrict on update restrict;

alter table BDF_R_COMPONENT add constraint FK_R_PA_R_B_R_PA13 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_) on delete restrict on update restrict;

alter table BDF_R_COMPONENT add constraint FK_Reference_31 foreign key (ACTION_DEF_ID_)
      references BDF_R_ACTION_DEF (ID_) on delete restrict on update restrict;

alter table BDF_R_COMPONENT add constraint FK_R_PA_R_B_R_EN7 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_) on delete restrict on update restrict;

alter table BDF_R_COMPONENT_EVENT add constraint FK_R_PA_R_B_R_CO5 foreign key (COMPONENT_ID_)
      references BDF_R_COMPONENT (ID_) on delete restrict on update restrict;

alter table BDF_R_COMPONENT_PROPERTY add constraint FK_R_PA_R_B_R_CO4 foreign key (COMPONENT_ID_)
      references BDF_R_COMPONENT (ID_) on delete restrict on update restrict;

alter table BDF_R_ENTITY add constraint FK_R_PA_R_B_R_PA14 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_) on delete restrict on update restrict;

alter table BDF_R_ENTITY add constraint FK_R_PA_R_B_R_EN18 foreign key (PARENT_ID_)
      references BDF_R_ENTITY (ID_) on delete restrict on update restrict;

alter table BDF_R_ENTITY_FIELD add constraint FK_R_PA_R_B_R_FI8 foreign key (METADATA_ID_)
      references BDF_R_FIELD_METADATA (ID_) on delete restrict on update restrict;

alter table BDF_R_ENTITY_FIELD add constraint FK_Reference_34 foreign key (MAPPING_ID_)
      references BDF_R_MAPPING (ID_);

alter table BDF_R_ENTITY_FIELD add constraint FK_R_PA_R_B_R_EN6 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_) on delete restrict on update restrict;

alter table BDF_R_ENTITY_PARAMETER add constraint FK_R_PA_R_B_R_EN19 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_) on delete restrict on update restrict;

alter table BDF_R_ENTITY_PARAMETER add constraint FK_R_PA_R_B_R_PA21 foreign key (PARAMETER_ID_)
      references BDF_R_PARAMETER (ID_) on delete restrict on update restrict;

alter table BDF_R_FIELD_METADATA add constraint FK_R_PA_R_B_R_PA15 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_) on delete restrict on update restrict;

alter table BDF_R_FIELD_METADATA add constraint FK_R_PA_R_B_R_ME17 foreign key (MAPPING_)
      references BDF_R_MAPPING (ID_) on delete restrict on update restrict;

alter table BDF_R_MAPPING add constraint FK_R_PA_R_B_R_PA22 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_) on delete restrict on update restrict;

alter table BDF_R_PACKAGE_INFO add constraint FK_R_PA_R_B_R_PA1 foreign key (PARENT_ID_)
      references BDF_R_PACKAGE_INFO (ID_) on delete restrict on update restrict;

alter table BDF_R_PAGE add constraint FK_R_PA_R_B_R_PA2 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_) on delete restrict on update restrict;

alter table BDF_R_PAGE_COMPONENT add constraint FK_R_PA_R_B_R_CO23 foreign key (COMPONENT_ID_)
      references BDF_R_COMPONENT (ID_) on delete restrict on update restrict;

alter table BDF_R_PAGE_COMPONENT add constraint FK_R_PA_R_B_R_PA3 foreign key (PAGE_ID_)
      references BDF_R_PAGE (ID_) on delete restrict on update restrict;

alter table BDF_R_PARAMETER add constraint FK_R_PA_R_B_R_PA16 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_) on delete restrict on update restrict;

alter table BDF_R_VALIDATOR_PROPERTY add constraint FK_Reference_35 foreign key (VALIDATOR_ID_)
      references BDF_R_VALIDATOR (ID_);

