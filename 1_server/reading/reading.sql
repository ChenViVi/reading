-- phpMyAdmin SQL Dump
-- version 4.0.10.20
-- https://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2018-05-18 15:03:22
-- 服务器版本: 5.7.22
-- PHP 版本: 5.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `reading`
--

-- --------------------------------------------------------

--
-- 表的结构 `action`
--

CREATE TABLE IF NOT EXISTS `action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `articleId` int(11) DEFAULT NULL,
  `favorite` int(1) DEFAULT '0',
  `collect` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=27 ;

--
-- 转存表中的数据 `action`
--

INSERT INTO `action` (`id`, `uid`, `articleId`, `favorite`, `collect`) VALUES
(25, 2, 1, 0, 1),
(26, 2, 3, 1, 1);

-- --------------------------------------------------------

--
-- 表的结构 `article`
--

CREATE TABLE IF NOT EXISTS `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL,
  `title` text,
  `info` varchar(8) DEFAULT NULL,
  `author` text,
  `date` text,
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- 转存表中的数据 `article`
--

INSERT INTO `article` (`id`, `type_id`, `title`, `info`, `author`, `date`, `content`) VALUES
(1, 1, '脑洞', '脑洞大开？', '中国数字科技馆', '2016.05.12', '其实，我们每个人刚出生时，就可以说是“脑洞大开”—生下来，脑袋上就自带两个“天窗”。这两个“天窗”是颅骨间的缝隙，分别被称为前囱( xin )门和后囱门。这两个囱门看起来像是发育不全，但却给了婴儿头骨一定的变形能力，使他们在出生时能更顺利地通过产道。这两个“脑洞”会随着人的发育自然闭合，后囱门在人出生后6一8周时就会闭合，前囱门在人出生后1一2岁的时候也会关上。如果闭合太早或者太晚，则可能意味着疾病。'),
(2, 1, '三岁见老', '“三岁见老”', '就医160', '2016.05.25', '俗话说，“三岁见老”，其实并非毫无依据的，从埃里克森的“心理社会发展理论”来看，儿童在三岁左右的时候便开始形成自己的行为习惯，习惯一旦养成，要改是很难很难的。都说养大一个孩子不容易，培养好一个孩子更不容易，究竟，小朋友的心理变化是怎样的一段奇妙之旅呢？'),
(3, 1, '药不能停', '“药不能停”，', '壹心理', '2016.05.26', '在很多人看来，和服药有关的众多事项中，最不需要注意的就是服药的次数和时间了，毕竟“药物只要吃下去就会治病，又何必在意什么时候吃？”这实际上是一种不折不扣的谬论，因为药品说明书上记载的用药次数、时间都不是空穴来风，如果不能做到遵守这些注意事项，轻则会影响药物的治疗效果，严重时甚至会产生额外的不良反应。药物的服药次数往往取决于它在血浆中的半衰期（即血浆浓度因人体的清除效应而下降一半所需的时间），因为只有血浆中的药物浓度达到了足以治疗疾病的水平，药物才能发挥正常效果。由于很多药物单次服用后的半衰期很短，因此为了令药物在服药期间能持续发挥效果，它们需要一天两次甚至多次服药；而对那些具有很长半衰期，或是能够在血浆浓度下降后依然会存在疗效（即“剂量后效应”）的药物，很多时候可能一天一次用药即足矣。至于服药时间上的安排，很多情况下这一点取决于药物及其所治疗疾病的特性。例如因肾上腺疾病而需要补充皮质类固醇的患者，往往需要在清晨和下午各服一剂药物，以模拟激素本身的分泌特点；需要控制餐后血糖的糖尿病患者，通常会在餐前使用降糖药物，这样既能够治疗餐后升高的血糖，又规避了降糖药物导致低血糖的作用。');

-- --------------------------------------------------------

--
-- 表的结构 `article_type`
--

CREATE TABLE IF NOT EXISTS `article_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `article_type`
--

INSERT INTO `article_type` (`id`, `name`) VALUES
(1, '默认');

-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `articleId` int(11) NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(100) NOT NULL,
  `favoriteCt` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- 转存表中的数据 `comment`
--

INSERT INTO `comment` (`id`, `uid`, `articleId`, `date`, `content`, `favoriteCt`) VALUES
(1, 2, 1, '2018-05-16 00:00:00', 'ssss', 0),
(2, 2, 1, '2018-05-09 00:00:00', 'wdf', 0),
(3, 2, 1, '2018-05-15 00:00:00', 'wdf', 0),
(4, 2, 3, '2018-05-08 00:00:00', '哈哈哈', 0),
(5, 2, 3, '2018-05-18 09:52:04', '哼哼哼', 2);

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) DEFAULT NULL,
  `password` varchar(16) DEFAULT NULL,
  `name` varchar(10) DEFAULT '读者',
  `sex` int(2) NOT NULL DEFAULT '0',
  `imgUrl` varchar(50) DEFAULT NULL,
  `sign` varchar(20) DEFAULT '我们没有什么不同',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `phone`, `password`, `name`, `sex`, `imgUrl`, `sign`) VALUES
(1, '15004112829', '123456', '读者1', 0, NULL, '我们没有什么不同'),
(2, '15334204967', 'qqqqqq', '读者2', 1, NULL, '我们没有什么不同');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
