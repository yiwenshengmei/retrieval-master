CREATE TABLE `t_matrix` (
  `mtx_id` varchar(40) NOT NULL,
  `mtx_node_id` varchar(40) NOT NULL,
  PRIMARY KEY (`mtx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_matrix_row` (
  `mtxr_id` varchar(40) NOT NULL,
  `mtxr_matrix_id` varchar(40) NOT NULL,
  `mtxr_row_index` int(11) NOT NULL,
  `mtxr_col_index` int(11) NOT NULL,
  `mtxr_value` int(11) DEFAULT NULL,
  PRIMARY KEY (`mtxr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_node` (
  `nd_id` varchar(40) NOT NULL,
  `nd_name` varchar(45) DEFAULT NULL,
  `nd_name_en` varchar(45) DEFAULT NULL,
  `nd_uri` varchar(45) DEFAULT NULL,
  `nd_uri_name` varchar(45) DEFAULT NULL,
  `nd_parent_id` varchar(45) DEFAULT NULL,
  `nd_detail_type` int(11) DEFAULT NULL,
  `nd_contact` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`nd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_node_attribute` (
  `attr_id` varchar(40) NOT NULL,
  `attr_name` varchar(45) DEFAULT NULL,
  `attr_name_en` varchar(45) DEFAULT NULL,
  `attr_node_id` varchar(40) DEFAULT NULL,
  `attr_desc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`attr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_node_field` (
  `uf_id` varchar(40) NOT NULL,
  `uf_key` varchar(40) DEFAULT NULL,
  `uf_value` varchar(40) DEFAULT NULL,
  `uf_node_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`uf_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_node_image` (
  `img_id` varchar(40) NOT NULL,
  `img_path` varchar(50) DEFAULT NULL,
  `img_node_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_user` (
  `u_id` varchar(40) NOT NULL,
  `u_name` varchar(40) NOT NULL,
  `u_password` varchar(20) DEFAULT NULL,
  `u_is_active` int(11) NOT NULL DEFAULT '0',
  `u_is_admin` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`u_id`),
  UNIQUE KEY `id_UNIQUE` (`u_id`),
  UNIQUE KEY `name_UNIQUE` (`u_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

