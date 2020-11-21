CREATE TABLE `utilisateur` (
  `id` int unsigned NOT NULL auto_increment,
  `nom` varchar(255) default NULL,
  `prenom` varchar(255) default NULL,
  `mail` varchar(255) default NULL,
  `mdp` varchar(255),
  PRIMARY KEY (`id`)
) AUTO_INCREMENT=1;



CREATE TABLE `tarifs` (
    `id` mediumint(8) unsigned NOT NULL auto_increment,
    `type_produit` varchar(255) default NULL,
    `prix` decimal(5,2) default 0,
    PRIMARY KEY (`id`)
) AUTO_INCREMENT=1;


create table abonnement
(
    id int auto_increment,
    date_debut datetime null,
    date_fin datetime null,
    id_tarif int null,
    constraint abonnement_pk
        primary key (id),
    constraint tarifs___fk
        foreign key (id_tarif) references tarifs (id)
            on update cascade on delete set null
) auto_increment = 1;


create table ticket
    (
    	id int auto_increment,
    	id_tarif int null,
    	nbVoyage int null,
    	constraint ticket_pk
    		primary key (id),
    	constraint ticket_tarifs_id_fk
    		foreign key (id_tarif) references tarifs (id)
    			on update cascade on delete set null
    )