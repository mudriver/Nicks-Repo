
CREATE TABLE `sent_email` (
  `id` int(11) NOT NULL,
  `type` varchar(10) NOT NULL,
  `email` text NOT NULL,
  `created_date` date NOT NULL
)
ALTER TABLE `sent_email`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `sent_email`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

