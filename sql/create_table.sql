DROP TABLE IF EXISTS user;

drop table user;

create table user
(
    id           bigint auto_increment primary key,
    username     varchar(256)                       null comment '⽤户昵称',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '⽤户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '状态 0-正常',
    phone        varchar(128)                       null comment '电话',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
)
comment '⽤户表';

