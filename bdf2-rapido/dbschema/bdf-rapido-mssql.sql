/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2005                    */
/* Created on:     2012/8/24 16:24:59                           */
/*==============================================================*/


if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ACTION_DEF') and o.name = 'FK_R_PA_R_B_R_EN11')
alter table BDF_R_ACTION_DEF
   drop constraint FK_R_PA_R_B_R_EN11
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ACTION_DEF') and o.name = 'FK_R_PA_R_B_R_PA12')
alter table BDF_R_ACTION_DEF
   drop constraint FK_R_PA_R_B_R_PA12
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ACTION_DEF_PARAMETER') and o.name = 'FK_R_PA_R_B_R_AC24')
alter table BDF_R_ACTION_DEF_PARAMETER
   drop constraint FK_R_PA_R_B_R_AC24
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ACTION_DEF_PARAMETER') and o.name = 'FK_R_PA_R_B_R_PA25')
alter table BDF_R_ACTION_DEF_PARAMETER
   drop constraint FK_R_PA_R_B_R_PA25
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ACTION_DEF_RELATION') and o.name = 'FK_R_PA_R_B_R_ACT')
alter table BDF_R_ACTION_DEF_RELATION
   drop constraint FK_R_PA_R_B_R_ACT
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ACTION_DEF_RELATION') and o.name = 'FK_R_PA_R_B_R_DA1')
alter table BDF_R_ACTION_DEF_RELATION
   drop constraint FK_R_PA_R_B_R_DA1
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ACTION_PARAMETER') and o.name = 'FK_BDF_R_AC_REFERENCE_BDF_R_AC')
alter table BDF_R_ACTION_PARAMETER
   drop constraint FK_BDF_R_AC_REFERENCE_BDF_R_AC
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ACTION_PARAMETER') and o.name = 'FK_BDF_R_AC_REFERENCE_BDF_R_PA')
alter table BDF_R_ACTION_PARAMETER
   drop constraint FK_BDF_R_AC_REFERENCE_BDF_R_PA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_COMPONENT') and o.name = 'FK_R_PA_R_B_R_CO9')
alter table BDF_R_COMPONENT
   drop constraint FK_R_PA_R_B_R_CO9
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_COMPONENT') and o.name = 'FK_R_PA_R_B_R_PA13')
alter table BDF_R_COMPONENT
   drop constraint FK_R_PA_R_B_R_PA13
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_COMPONENT') and o.name = 'FK_BDF_R_CO_REFERENCE_BDF_R_AC')
alter table BDF_R_COMPONENT
   drop constraint FK_BDF_R_CO_REFERENCE_BDF_R_AC
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_COMPONENT') and o.name = 'FK_R_PA_R_B_R_EN7')
alter table BDF_R_COMPONENT
   drop constraint FK_R_PA_R_B_R_EN7
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_COMPONENT_EVENT') and o.name = 'FK_R_PA_R_B_R_CO5')
alter table BDF_R_COMPONENT_EVENT
   drop constraint FK_R_PA_R_B_R_CO5
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_COMPONENT_PROPERTY') and o.name = 'FK_R_PA_R_B_R_CO4')
alter table BDF_R_COMPONENT_PROPERTY
   drop constraint FK_R_PA_R_B_R_CO4
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ENTITY') and o.name = 'FK_R_PA_R_B_R_PA14')
alter table BDF_R_ENTITY
   drop constraint FK_R_PA_R_B_R_PA14
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ENTITY') and o.name = 'FK_R_PA_R_B_R_EN18')
alter table BDF_R_ENTITY
   drop constraint FK_R_PA_R_B_R_EN18
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ENTITY_FIELD') and o.name = 'FK_R_PA_R_B_R_FI8')
alter table BDF_R_ENTITY_FIELD
   drop constraint FK_R_PA_R_B_R_FI8
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ENTITY_FIELD') and o.name = 'FK_BDF_R_EN_REFERENCE_BDF_R_MA')
alter table BDF_R_ENTITY_FIELD
   drop constraint FK_BDF_R_EN_REFERENCE_BDF_R_MA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ENTITY_FIELD') and o.name = 'FK_R_PA_R_B_R_EN6')
alter table BDF_R_ENTITY_FIELD
   drop constraint FK_R_PA_R_B_R_EN6
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ENTITY_PARAMETER') and o.name = 'FK_R_PA_R_B_R_EN19')
alter table BDF_R_ENTITY_PARAMETER
   drop constraint FK_R_PA_R_B_R_EN19
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_ENTITY_PARAMETER') and o.name = 'FK_R_PA_R_B_R_PA21')
alter table BDF_R_ENTITY_PARAMETER
   drop constraint FK_R_PA_R_B_R_PA21
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_FIELD_METADATA') and o.name = 'FK_R_PA_R_B_R_PA15')
alter table BDF_R_FIELD_METADATA
   drop constraint FK_R_PA_R_B_R_PA15
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_FIELD_METADATA') and o.name = 'FK_R_PA_R_B_R_ME17')
alter table BDF_R_FIELD_METADATA
   drop constraint FK_R_PA_R_B_R_ME17
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_MAPPING') and o.name = 'FK_R_PA_R_B_R_PA22')
alter table BDF_R_MAPPING
   drop constraint FK_R_PA_R_B_R_PA22
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_PACKAGE_INFO') and o.name = 'FK_R_PA_R_B_R_PA1')
alter table BDF_R_PACKAGE_INFO
   drop constraint FK_R_PA_R_B_R_PA1
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_PAGE') and o.name = 'FK_R_PA_R_B_R_PA2')
alter table BDF_R_PAGE
   drop constraint FK_R_PA_R_B_R_PA2
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_PAGE_COMPONENT') and o.name = 'FK_R_PA_R_B_R_CO23')
alter table BDF_R_PAGE_COMPONENT
   drop constraint FK_R_PA_R_B_R_CO23
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_PAGE_COMPONENT') and o.name = 'FK_R_PA_R_B_R_PA3')
alter table BDF_R_PAGE_COMPONENT
   drop constraint FK_R_PA_R_B_R_PA3
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_PARAMETER') and o.name = 'FK_R_PA_R_B_R_PA16')
alter table BDF_R_PARAMETER
   drop constraint FK_R_PA_R_B_R_PA16
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BDF_R_VALIDATOR_PROPERTY') and o.name = 'FK_BDF_R_VA_REFERENCE_BDF_R_VA')
alter table BDF_R_VALIDATOR_PROPERTY
   drop constraint FK_BDF_R_VA_REFERENCE_BDF_R_VA
go

alter table BDF_R_ACTION
   drop constraint PK_BDF_R_ACTION
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_ACTION')
            and   type = 'U')
   drop table BDF_R_ACTION
go

alter table BDF_R_ACTION_DEF
   drop constraint PK_BDF_R_ACTION_DEF
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_ACTION_DEF')
            and   type = 'U')
   drop table BDF_R_ACTION_DEF
go

alter table BDF_R_ACTION_DEF_PARAMETER
   drop constraint PK_BDF_R_ACTION_DEF_PARAMETER
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_ACTION_DEF_PARAMETER')
            and   type = 'U')
   drop table BDF_R_ACTION_DEF_PARAMETER
go

alter table BDF_R_ACTION_DEF_RELATION
   drop constraint PK_BDF_R_ACTION_DEF_RELATION
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_ACTION_DEF_RELATION')
            and   type = 'U')
   drop table BDF_R_ACTION_DEF_RELATION
go

alter table BDF_R_ACTION_PARAMETER
   drop constraint PK_BDF_R_ACTION_PARAMETER
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_ACTION_PARAMETER')
            and   type = 'U')
   drop table BDF_R_ACTION_PARAMETER
go

alter table BDF_R_COMPONENT
   drop constraint PK_BDF_R_COMPONENT
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_COMPONENT')
            and   type = 'U')
   drop table BDF_R_COMPONENT
go

alter table BDF_R_COMPONENT_EVENT
   drop constraint PK_BDF_R_COMPONENT_EVENT
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_COMPONENT_EVENT')
            and   type = 'U')
   drop table BDF_R_COMPONENT_EVENT
go

alter table BDF_R_COMPONENT_PROPERTY
   drop constraint PK_BDF_R_COMPONENT_PROPERTY
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_COMPONENT_PROPERTY')
            and   type = 'U')
   drop table BDF_R_COMPONENT_PROPERTY
go

alter table BDF_R_ENTITY
   drop constraint PK_BDF_R_ENTITY
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_ENTITY')
            and   type = 'U')
   drop table BDF_R_ENTITY
go

alter table BDF_R_ENTITY_FIELD
   drop constraint PK_BDF_R_ENTITY_FIELD
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_ENTITY_FIELD')
            and   type = 'U')
   drop table BDF_R_ENTITY_FIELD
go

alter table BDF_R_ENTITY_PARAMETER
   drop constraint PK_BDF_R_ENTITY_PARAMETER
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_ENTITY_PARAMETER')
            and   type = 'U')
   drop table BDF_R_ENTITY_PARAMETER
go

alter table BDF_R_FIELD_METADATA
   drop constraint PK_BDF_R_FIELD_METADATA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_FIELD_METADATA')
            and   type = 'U')
   drop table BDF_R_FIELD_METADATA
go

alter table BDF_R_LAYOUT_CONSTRAINT_PROP
   drop constraint PK_B_R_LT_CONS_P1
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_LAYOUT_CONSTRAINT_PROP')
            and   type = 'U')
   drop table BDF_R_LAYOUT_CONSTRAINT_PROP
go

alter table BDF_R_LAYOUT_PROPERTY
   drop constraint PK_BDF_R_LAYOUT_PROPERTY
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_LAYOUT_PROPERTY')
            and   type = 'U')
   drop table BDF_R_LAYOUT_PROPERTY
go

alter table BDF_R_MAPPING
   drop constraint PK_BDF_R_MAPPING
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_MAPPING')
            and   type = 'U')
   drop table BDF_R_MAPPING
go

alter table BDF_R_PACKAGE_INFO
   drop constraint PK_BDF_R_PACKAGE_INFO
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_PACKAGE_INFO')
            and   type = 'U')
   drop table BDF_R_PACKAGE_INFO
go

alter table BDF_R_PAGE
   drop constraint PK_BDF_R_PAGE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_PAGE')
            and   type = 'U')
   drop table BDF_R_PAGE
go

alter table BDF_R_PAGE_COMPONENT
   drop constraint PK_BDF_R_PAGE_COMPONENT
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_PAGE_COMPONENT')
            and   type = 'U')
   drop table BDF_R_PAGE_COMPONENT
go

alter table BDF_R_PARAMETER
   drop constraint PK_BDF_R_PARAMETER
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_PARAMETER')
            and   type = 'U')
   drop table BDF_R_PARAMETER
go

alter table BDF_R_VALIDATOR
   drop constraint PK_BDF_R_VALIDATOR
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_VALIDATOR')
            and   type = 'U')
   drop table BDF_R_VALIDATOR
go

alter table BDF_R_VALIDATOR_PROPERTY
   drop constraint PK_BDF_R_VALIDATOR_PROPERTY
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BDF_R_VALIDATOR_PROPERTY')
            and   type = 'U')
   drop table BDF_R_VALIDATOR_PROPERTY
go

/*==============================================================*/
/* Table: BDF_R_ACTION                                          */
/*==============================================================*/
create table BDF_R_ACTION (
   ID_                  varchar(50)          not null,
   NAME_                varchar(250)         null,
   BEAN_ID_             varchar(100)         null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '具体动作信息表',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '名称',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '定义在Spring当中的BeanID',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION', 'column', 'BEAN_ID_'
go

alter table BDF_R_ACTION
   add constraint PK_BDF_R_ACTION primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_ACTION_DEF                                      */
/*==============================================================*/
create table BDF_R_ACTION_DEF (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   DESC_                varchar(50)          null,
   TYPE_                varchar(10)          null,
   SCRIPT_              varchar(1000)        null,
   ENTITY_ID_           varchar(50)          null,
   ASYNC_               char(1)              null,
   CONFIRM_MESSAGE_     varchar(100)         null,
   SUCCESS_MESSAGE_     varchar(100)         null,
   BEFORE_EXECUTE_SCRIPT_ varchar(1000)        null,
   ON_SUCCESS_SCRIPT_   varchar(1000)        null,
   PACKAGE_ID_          varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '目前只支持两种类型的动作：ajaxAction及updateAction，分别对应d7中的两种类型的action',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '动作名称',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '两种类型A(ajax)及U(update)',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'TYPE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '动作脚本',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'SCRIPT_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '动作涉及到的实体对象',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'ENTITY_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否采用异步执行',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'ASYNC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '执行前确认消息',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'CONFIRM_MESSAGE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '执行成功后消息',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'SUCCESS_MESSAGE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '执行动作前的事件脚本',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'BEFORE_EXECUTE_SCRIPT_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '执行动作成功后的事件脚本',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'ON_SUCCESS_SCRIPT_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所在包',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF', 'column', 'PACKAGE_ID_'
go

alter table BDF_R_ACTION_DEF
   add constraint PK_BDF_R_ACTION_DEF primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_ACTION_DEF_PARAMETER                            */
/*==============================================================*/
create table BDF_R_ACTION_DEF_PARAMETER (
   ID_                  varchar(50)          not null,
   ACTION_DEF_ID_       varchar(50)          null,
   PARAMETER_ID_        varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '动作参数关系表',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF_PARAMETER'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF_PARAMETER', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所属动作ID',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF_PARAMETER', 'column', 'ACTION_DEF_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所属参数ID',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF_PARAMETER', 'column', 'PARAMETER_ID_'
go

alter table BDF_R_ACTION_DEF_PARAMETER
   add constraint PK_BDF_R_ACTION_DEF_PARAMETER primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_ACTION_DEF_RELATION                             */
/*==============================================================*/
create table BDF_R_ACTION_DEF_RELATION (
   ID_                  varchar(50)          not null,
   ACTION_ID_           varchar(50)          null,
   ACTION_DEF_ID_       varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '动作定义关系表',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF_RELATION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF_RELATION', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '动作ID',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF_RELATION', 'column', 'ACTION_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '动作定义ID',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_DEF_RELATION', 'column', 'ACTION_DEF_ID_'
go

alter table BDF_R_ACTION_DEF_RELATION
   add constraint PK_BDF_R_ACTION_DEF_RELATION primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_ACTION_PARAMETER                                */
/*==============================================================*/
create table BDF_R_ACTION_PARAMETER (
   ID_                  varchar(50)          not null,
   ACTION_ID_           varchar(50)          null,
   PARAMETER_ID_        varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '具体动作参数关系表',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_PARAMETER'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_PARAMETER', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '具体动作ID',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_PARAMETER', 'column', 'ACTION_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '参数ID',
   'user', @CurrentUser, 'table', 'BDF_R_ACTION_PARAMETER', 'column', 'PARAMETER_ID_'
go

alter table BDF_R_ACTION_PARAMETER
   add constraint PK_BDF_R_ACTION_PARAMETER primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_COMPONENT                                       */
/*==============================================================*/
create table BDF_R_COMPONENT (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   DESC_                varchar(50)          null,
   CLASS_NAME_          varchar(250)         null,
   ENTITY_ID_           varchar(50)          null,
   PARENT_ID_           varchar(50)          null,
   LAYOUT_              varchar(20)          null,
   ACTION_DEF_ID_       varchar(50)          null,
   CONTAINER_           char(1)              null,
   PACKAGE_ID_          varchar(50)          null,
   ORDER_               int                  null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'Dorado7组件信息表',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '组件名称',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '组件实现类名',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'CLASS_NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '采用的实体对象',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'ENTITY_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '通过该属性组件之间嵌套，比如AutoForm与其下的Element；Toolbar与其Toolbutton，Grid与其Column等',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'PARENT_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '采用的布局',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'LAYOUT_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '采用的动作定义ID',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'ACTION_DEF_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '1表示为容器型，0表示非容器型，容器型组件下可放其它组件',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'CONTAINER_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所在包',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'PACKAGE_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '排序号',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT', 'column', 'ORDER_'
go

alter table BDF_R_COMPONENT
   add constraint PK_BDF_R_COMPONENT primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_COMPONENT_EVENT                                 */
/*==============================================================*/
create table BDF_R_COMPONENT_EVENT (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   DESC_                varchar(50)          null,
   SCRIPT_              varchar(2000)        null,
   COMPONENT_ID_        varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '组件的事件信息表',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_EVENT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_EVENT', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '事件名称',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_EVENT', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_EVENT', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '事件脚本内容',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_EVENT', 'column', 'SCRIPT_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所属组件',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_EVENT', 'column', 'COMPONENT_ID_'
go

alter table BDF_R_COMPONENT_EVENT
   add constraint PK_BDF_R_COMPONENT_EVENT primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_COMPONENT_PROPERTY                              */
/*==============================================================*/
create table BDF_R_COMPONENT_PROPERTY (
   ID_                  varchar(50)          not null,
   NAME_                varchar(100)         null,
   VALUE_               varchar(250)         null,
   COMPONENT_ID_        varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '组件的属性信息',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_PROPERTY'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_PROPERTY', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '属性名称',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_PROPERTY', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '属性值',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_PROPERTY', 'column', 'VALUE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '隶属组件',
   'user', @CurrentUser, 'table', 'BDF_R_COMPONENT_PROPERTY', 'column', 'COMPONENT_ID_'
go

alter table BDF_R_COMPONENT_PROPERTY
   add constraint PK_BDF_R_COMPONENT_PROPERTY primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_ENTITY                                          */
/*==============================================================*/
create table BDF_R_ENTITY (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   TABLE_NAME_          varchar(100)         null,
   RECURSIVE_           char(1)              null,
   DESC_                varchar(50)          null,
   QUERY_SQL_           varchar(1000)        null,
   PACKAGE_ID_          varchar(50)          null,
   PAGE_SIZE_           int                  null,
   PARENT_ID_           varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '对应数据库中定义的表或视图',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '实体名称',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表示该实体对象要操作的主表名称',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY', 'column', 'TABLE_NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否为递归结构',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY', 'column', 'RECURSIVE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '查询用的SQL',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY', 'column', 'QUERY_SQL_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所在包',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY', 'column', 'PACKAGE_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '每页显示记录数',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY', 'column', 'PAGE_SIZE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '隶属实体对象ID',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY', 'column', 'PARENT_ID_'
go

alter table BDF_R_ENTITY
   add constraint PK_BDF_R_ENTITY primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_ENTITY_FIELD                                    */
/*==============================================================*/
create table BDF_R_ENTITY_FIELD (
   ID_                  varchar(50)          not null,
   NAME_                varchar(100)         null,
   READ_ONLY_           char(1)              null,
   SUBMITTABLE_         char(1)              null,
   ENTITY_ID_           varchar(50)          null,
   DESC_                varchar(50)          null,
   METADATA_ID_         varchar(50)          null,
   TABLE_NAME_          varchar(100)         null,
   PRIMARY_KEY_         char(1)              null,
   KEY_GENERATE_TYPE_   varchar(20)          null,
   KEY_GENERATOR_       varchar(100)         null,
   MAPPING_ID_          varchar(50)          null,
   DATA_TYPE_           varchar(50)          null,
   LABEL_               varchar(50)          null,
   REQUIRED_            char(1)              null,
   DEFAULT_VALUE_       varchar(50)          null,
   DISPLAY_FORMAT_      varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '实体字段信息表',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '字段名',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '0表示只读，1表示非只读',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'READ_ONLY_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否要提交数据到后台',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'SUBMITTABLE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所属实体对象',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'ENTITY_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '采用的元数据对象',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'METADATA_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '隶属表',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'TABLE_NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否为主键',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'PRIMARY_KEY_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '生成方式有:custom、autoincrement、sequence',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'KEY_GENERATE_TYPE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '可能是具体的EL表达式或一个具体的sequence对象',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'KEY_GENERATOR_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '采用的Mapping',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'MAPPING_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据类型',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'DATA_TYPE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '字段标题',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'LABEL_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否为必须',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'REQUIRED_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '默认值',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'DEFAULT_VALUE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '显示格式',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_FIELD', 'column', 'DISPLAY_FORMAT_'
go

alter table BDF_R_ENTITY_FIELD
   add constraint PK_BDF_R_ENTITY_FIELD primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_ENTITY_PARAMETER                                */
/*==============================================================*/
create table BDF_R_ENTITY_PARAMETER (
   ID_                  varchar(50)          not null,
   ENTITY_ID_           varchar(50)          null,
   PARAMETER_ID_        varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '实体对象查询条件参数表',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_PARAMETER'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_PARAMETER', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '隶属实体对象ID',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_PARAMETER', 'column', 'ENTITY_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '参数ID',
   'user', @CurrentUser, 'table', 'BDF_R_ENTITY_PARAMETER', 'column', 'PARAMETER_ID_'
go

alter table BDF_R_ENTITY_PARAMETER
   add constraint PK_BDF_R_ENTITY_PARAMETER primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_FIELD_METADATA                                  */
/*==============================================================*/
create table BDF_R_FIELD_METADATA (
   ID_                  varchar(50)          not null,
   NAME_                varchar(100)         null,
   DESC_                varchar(50)          null,
   DEFAULT_VALUE_       varchar(50)          null,
   DISPLAY_FORMAT_      varchar(50)          null,
   REQUIRED_            char(1)              null,
   LABEL_               varchar(100)         null,
   PACKAGE_ID_          varchar(50)          null,
   MAPPING_             varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '字段元数据信息表',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '字段名',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '默认值',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA', 'column', 'DEFAULT_VALUE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '显示格式',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA', 'column', 'DISPLAY_FORMAT_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否为必须',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA', 'column', 'REQUIRED_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '字段标题',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA', 'column', 'LABEL_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所在包',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA', 'column', 'PACKAGE_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据映射',
   'user', @CurrentUser, 'table', 'BDF_R_FIELD_METADATA', 'column', 'MAPPING_'
go

alter table BDF_R_FIELD_METADATA
   add constraint PK_BDF_R_FIELD_METADATA primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_LAYOUT_CONSTRAINT_PROP                          */
/*==============================================================*/
create table BDF_R_LAYOUT_CONSTRAINT_PROP (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   VALUE_               varchar(20)          null,
   DESC_                varchar(50)          null,
   COMPONENT_ID_        varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '组件约束属性信息表',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_CONSTRAINT_PROP'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_CONSTRAINT_PROP', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '约束属性名称',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_CONSTRAINT_PROP', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '约束属性值',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_CONSTRAINT_PROP', 'column', 'VALUE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_CONSTRAINT_PROP', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '该字段中存储的是BDF_R_COMPONENT表主键值或BDF_R_PAGE_COMPONENT表主键值或BDF_RG_PAGE_COMPONENT表主键值',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_CONSTRAINT_PROP', 'column', 'COMPONENT_ID_'
go

alter table BDF_R_LAYOUT_CONSTRAINT_PROP
   add constraint PK_B_R_LT_CONS_P1 primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_LAYOUT_PROPERTY                                 */
/*==============================================================*/
create table BDF_R_LAYOUT_PROPERTY (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   VALUE_               varchar(20)          null,
   DESC_                varchar(50)          null,
   COMPONENT_ID_        varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '组件布局属性信息表',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_PROPERTY'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_PROPERTY', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '属性名称',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_PROPERTY', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '属性值',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_PROPERTY', 'column', 'VALUE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_PROPERTY', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '该字段中存储的是BDF_R_COMPONENT表主键值或BDF_R_PAGE_COMPONENT表主键值或BDF_RG_PAGE_COMPONENT表主键值',
   'user', @CurrentUser, 'table', 'BDF_R_LAYOUT_PROPERTY', 'column', 'COMPONENT_ID_'
go

alter table BDF_R_LAYOUT_PROPERTY
   add constraint PK_BDF_R_LAYOUT_PROPERTY primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_MAPPING                                         */
/*==============================================================*/
create table BDF_R_MAPPING (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   SOURCE_              varchar(10)          null,
   VALUE_FIELD_         varchar(100)         null,
   KEY_FIELD_           varchar(100)         null,
   QUERY_SQL_           varchar(250)         null,
   CUSTOM_KEY_VALUE_    varchar(250)         null,
   PACKAGE_ID_          varchar(50)          null,
   PROPERTY_            varchar(100)         null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '值映射信息表',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '名称',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'custom表示用户自定义；table表示数据库中的表',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING', 'column', 'SOURCE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '用于显示的字段名',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING', 'column', 'VALUE_FIELD_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '用于实际值的字段名',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING', 'column', 'KEY_FIELD_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '查询表中键值所用SQL',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING', 'column', 'QUERY_SQL_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '格式为：key1=value1;key2=value2',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING', 'column', 'CUSTOM_KEY_VALUE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所在包',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING', 'column', 'PACKAGE_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '下拉框值回填属性名',
   'user', @CurrentUser, 'table', 'BDF_R_MAPPING', 'column', 'PROPERTY_'
go

alter table BDF_R_MAPPING
   add constraint PK_BDF_R_MAPPING primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_PACKAGE_INFO                                    */
/*==============================================================*/
create table BDF_R_PACKAGE_INFO (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   PARENT_ID_           varchar(50)          null,
   TYPE_                varchar(10)          null,
   DESC_                varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '包信息表',
   'user', @CurrentUser, 'table', 'BDF_R_PACKAGE_INFO'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_PACKAGE_INFO', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '目录名称',
   'user', @CurrentUser, 'table', 'BDF_R_PACKAGE_INFO', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '上级目录ID',
   'user', @CurrentUser, 'table', 'BDF_R_PACKAGE_INFO', 'column', 'PARENT_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'page用于存放主页面；subpage用于存放子页面；component用于存放组件；action用于存放动作;entity用于存储实体;parameter用于存储参数;metadata用于存储元数据',
   'user', @CurrentUser, 'table', 'BDF_R_PACKAGE_INFO', 'column', 'TYPE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述用于显示',
   'user', @CurrentUser, 'table', 'BDF_R_PACKAGE_INFO', 'column', 'DESC_'
go

alter table BDF_R_PACKAGE_INFO
   add constraint PK_BDF_R_PACKAGE_INFO primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_PAGE                                            */
/*==============================================================*/
create table BDF_R_PAGE (
   ID_                  varchar(50)          not null,
   NAME_                varchar(100)         null,
   PACKAGE_ID_          varchar(50)          null,
   LAYOUT_              varchar(20)          null,
   DESC_                varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主页面信息表',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '页面名称',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所在包',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE', 'column', 'PACKAGE_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '采用的布局',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE', 'column', 'LAYOUT_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE', 'column', 'DESC_'
go

alter table BDF_R_PAGE
   add constraint PK_BDF_R_PAGE primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_PAGE_COMPONENT                                  */
/*==============================================================*/
create table BDF_R_PAGE_COMPONENT (
   ID_                  varchar(50)          not null,
   PAGE_ID_             varchar(50)          null,
   ORDER_               int                  null,
   COMPONENT_ID_        varchar(50)          null,
   READ_ONLY_           char(1)              null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主页面的组件集信息表',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE_COMPONENT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE_COMPONENT', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所属主页面',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE_COMPONENT', 'column', 'PAGE_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '排序号',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE_COMPONENT', 'column', 'ORDER_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '对应的组件ID',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE_COMPONENT', 'column', 'COMPONENT_ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '如果为只读，那么该组件及其下所有组件生成时都自动添加一个readOnly属性',
   'user', @CurrentUser, 'table', 'BDF_R_PAGE_COMPONENT', 'column', 'READ_ONLY_'
go

alter table BDF_R_PAGE_COMPONENT
   add constraint PK_BDF_R_PAGE_COMPONENT primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_PARAMETER                                       */
/*==============================================================*/
create table BDF_R_PARAMETER (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   DESC_                varchar(50)          null,
   VALUE_               varchar(100)         null,
   TYPE_                varchar(10)          null,
   PACKAGE_ID_          varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '查询更新参数信息表',
   'user', @CurrentUser, 'table', 'BDF_R_PARAMETER'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_PARAMETER', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '参数名',
   'user', @CurrentUser, 'table', 'BDF_R_PARAMETER', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_PARAMETER', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '参数值可以是一个固定的值，也可以是一个EL表达式，如${abc},或者以#{开头，表示一个BSH表达式；或者为空，为空表示加载数据时从前台传入的参数中取',
   'user', @CurrentUser, 'table', 'BDF_R_PARAMETER', 'column', 'VALUE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'query表示查询，update表示更新，insert表示新增',
   'user', @CurrentUser, 'table', 'BDF_R_PARAMETER', 'column', 'TYPE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '所在包',
   'user', @CurrentUser, 'table', 'BDF_R_PARAMETER', 'column', 'PACKAGE_ID_'
go

alter table BDF_R_PARAMETER
   add constraint PK_BDF_R_PARAMETER primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_VALIDATOR                                       */
/*==============================================================*/
create table BDF_R_VALIDATOR (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   DESC_                varchar(50)          null,
   FIELD_ID_            varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '字段验证器信息表',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '验证器名称',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '描述',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR', 'column', 'DESC_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '可能是Mapping表的ID，也可能是实体字段表的ID',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR', 'column', 'FIELD_ID_'
go

alter table BDF_R_VALIDATOR
   add constraint PK_BDF_R_VALIDATOR primary key nonclustered (ID_)
go

/*==============================================================*/
/* Table: BDF_R_VALIDATOR_PROPERTY                              */
/*==============================================================*/
create table BDF_R_VALIDATOR_PROPERTY (
   ID_                  varchar(50)          not null,
   NAME_                varchar(50)          null,
   VALUE_               varchar(200)         null,
   VALIDATOR_ID_        varchar(50)          null
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '验证器属性信息表',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR_PROPERTY'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '主键',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR_PROPERTY', 'column', 'ID_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '属性名',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR_PROPERTY', 'column', 'NAME_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '属性值',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR_PROPERTY', 'column', 'VALUE_'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '隶属验证器',
   'user', @CurrentUser, 'table', 'BDF_R_VALIDATOR_PROPERTY', 'column', 'VALIDATOR_ID_'
go

alter table BDF_R_VALIDATOR_PROPERTY
   add constraint PK_BDF_R_VALIDATOR_PROPERTY primary key nonclustered (ID_)
go

alter table BDF_R_ACTION_DEF
   add constraint FK_R_PA_R_B_R_EN11 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_)
go

alter table BDF_R_ACTION_DEF
   add constraint FK_R_PA_R_B_R_PA12 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
go

alter table BDF_R_ACTION_DEF_PARAMETER
   add constraint FK_R_PA_R_B_R_AC24 foreign key (ACTION_DEF_ID_)
      references BDF_R_ACTION_DEF (ID_)
go

alter table BDF_R_ACTION_DEF_PARAMETER
   add constraint FK_R_PA_R_B_R_PA25 foreign key (PARAMETER_ID_)
      references BDF_R_PARAMETER (ID_)
go

alter table BDF_R_ACTION_DEF_RELATION
   add constraint FK_R_PA_R_B_R_ACT foreign key (ACTION_ID_)
      references BDF_R_ACTION (ID_)
go

alter table BDF_R_ACTION_DEF_RELATION
   add constraint FK_R_PA_R_B_R_DA1 foreign key (ACTION_DEF_ID_)
      references BDF_R_ACTION_DEF (ID_)
go

alter table BDF_R_ACTION_PARAMETER
   add constraint FK_BDF_R_AC_REFERENCE_BDF_R_AC foreign key (ACTION_ID_)
      references BDF_R_ACTION (ID_)
go

alter table BDF_R_ACTION_PARAMETER
   add constraint FK_BDF_R_AC_REFERENCE_BDF_R_PA foreign key (PARAMETER_ID_)
      references BDF_R_PARAMETER (ID_)
go

alter table BDF_R_COMPONENT
   add constraint FK_R_PA_R_B_R_CO9 foreign key (PARENT_ID_)
      references BDF_R_COMPONENT (ID_)
go

alter table BDF_R_COMPONENT
   add constraint FK_R_PA_R_B_R_PA13 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
go

alter table BDF_R_COMPONENT
   add constraint FK_BDF_R_CO_REFERENCE_BDF_R_AC foreign key (ACTION_DEF_ID_)
      references BDF_R_ACTION_DEF (ID_)
go

alter table BDF_R_COMPONENT
   add constraint FK_R_PA_R_B_R_EN7 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_)
go

alter table BDF_R_COMPONENT_EVENT
   add constraint FK_R_PA_R_B_R_CO5 foreign key (COMPONENT_ID_)
      references BDF_R_COMPONENT (ID_)
go

alter table BDF_R_COMPONENT_PROPERTY
   add constraint FK_R_PA_R_B_R_CO4 foreign key (COMPONENT_ID_)
      references BDF_R_COMPONENT (ID_)
go

alter table BDF_R_ENTITY
   add constraint FK_R_PA_R_B_R_PA14 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
go

alter table BDF_R_ENTITY
   add constraint FK_R_PA_R_B_R_EN18 foreign key (PARENT_ID_)
      references BDF_R_ENTITY (ID_)
go

alter table BDF_R_ENTITY_FIELD
   add constraint FK_R_PA_R_B_R_FI8 foreign key (METADATA_ID_)
      references BDF_R_FIELD_METADATA (ID_)
go

alter table BDF_R_ENTITY_FIELD
   add constraint FK_BDF_R_EN_REFERENCE_BDF_R_MA foreign key (MAPPING_ID_)
      references BDF_R_MAPPING (ID_)
go

alter table BDF_R_ENTITY_FIELD
   add constraint FK_R_PA_R_B_R_EN6 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_)
go

alter table BDF_R_ENTITY_PARAMETER
   add constraint FK_R_PA_R_B_R_EN19 foreign key (ENTITY_ID_)
      references BDF_R_ENTITY (ID_)
go

alter table BDF_R_ENTITY_PARAMETER
   add constraint FK_R_PA_R_B_R_PA21 foreign key (PARAMETER_ID_)
      references BDF_R_PARAMETER (ID_)
go

alter table BDF_R_FIELD_METADATA
   add constraint FK_R_PA_R_B_R_PA15 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
go

alter table BDF_R_FIELD_METADATA
   add constraint FK_R_PA_R_B_R_ME17 foreign key (MAPPING_)
      references BDF_R_MAPPING (ID_)
go

alter table BDF_R_MAPPING
   add constraint FK_R_PA_R_B_R_PA22 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
go

alter table BDF_R_PACKAGE_INFO
   add constraint FK_R_PA_R_B_R_PA1 foreign key (PARENT_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
go

alter table BDF_R_PAGE
   add constraint FK_R_PA_R_B_R_PA2 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
go

alter table BDF_R_PAGE_COMPONENT
   add constraint FK_R_PA_R_B_R_CO23 foreign key (COMPONENT_ID_)
      references BDF_R_COMPONENT (ID_)
go

alter table BDF_R_PAGE_COMPONENT
   add constraint FK_R_PA_R_B_R_PA3 foreign key (PAGE_ID_)
      references BDF_R_PAGE (ID_)
go

alter table BDF_R_PARAMETER
   add constraint FK_R_PA_R_B_R_PA16 foreign key (PACKAGE_ID_)
      references BDF_R_PACKAGE_INFO (ID_)
go

alter table BDF_R_VALIDATOR_PROPERTY
   add constraint FK_BDF_R_VA_REFERENCE_BDF_R_VA foreign key (VALIDATOR_ID_)
      references BDF_R_VALIDATOR (ID_)
go

