INSERT INTO `event_category`
VALUES (160, 'Category1', NULL, '2016-08-10 12:06:26'), (161, 'Category2', NULL, '2016-08-10 12:06:26'),
  (162, 'Uncatigorized', NULL, '2016-08-10 12:06:31');

INSERT INTO `event` VALUES
  (127, 'Event1', 'Event1 short description', 'Event1 full description', 'Yerevan, Armenia', NULL, NULL, NULL, NULL,
        160, 1, 0, '2016-08-10 12:09:26', '2016-08-10 12:09:26', '2016-08-10 12:09:26', NULL),
  (128, 'Event2', 'Event2 short description', 'Event2 full description', 'Gyumri, Armenia', NULL, NULL, NULL, NULL, 161,
        1, 0, '2016-08-10 12:10:10', '2016-08-10 12:10:10', '2016-08-10 12:10:10', NULL),
  (129, 'Event3', 'Event3 short description', 'Event3 full description', 'Yerevan, Armenia', NULL, NULL, NULL, NULL,
        160, 1, 0, '2016-08-31 17:27:25', '2016-08-10 12:10:10', '2016-08-10 12:10:10', NULL);

INSERT INTO `user` VALUES
  (355, 'Hermine', 'Turshujyan', '7962468883c36dc5666c3aaa3bb3d38d0900ec827c203dd53d86babf0c3094e6',
   'turshujyan@gmail.com', '', NULL, 0, '2016-08-26 12:49:15');


INSERT INTO `user_response` VALUES (3, 'Maybe'), (2, 'No'), (4, 'Undefined'), (1, 'Yes'), (5, 'Waiting for response');