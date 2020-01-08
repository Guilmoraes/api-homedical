INSERT INTO document_type (id, name, required, created_by, created_date, last_modified_by, last_modified_date) VALUES ('b62bc22f-621d-4089-b294-bd9a08268e02', 'CPF', 1,  'admin', '2018-03-17 13:38:17', 'admin', '2018-03-17 13:38:17');

INSERT INTO document (id,url,file_name,s3_name,file,file_content_type,processed,created_by,created_date,last_modified_by,last_modified_date,status,type_id,professional_id)
VALUES ('b62bc22f-621d-4089-b294-bd9a08268000',
            'www.teste.com/18Hn4/homedical.png',
            'homedical.png',
            '18Hn4/homedical.png',
            null,
            'image/jpg',
            0,
            'admin',
            '2018-03-17 13:38:17',
            'admin',
            '2018-03-17 13:38:17',
            'WAITING_APPROVEMENT',
            'b62bc22f-621d-4089-b294-bd9a08268e02',
            '179a277e-b7a8-4e86-90f5-d5cb4b94465d'
        );




