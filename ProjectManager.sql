USE [master]
GO
/****** Object:  Database [ProjectManager]    Script Date: 29 04 2023 15:50:58 ******/
CREATE DATABASE [ProjectManager]
 CONTAINMENT = NONE
GO
ALTER DATABASE [ProjectManager] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [ProjectManager].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [ProjectManager] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [ProjectManager] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [ProjectManager] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [ProjectManager] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [ProjectManager] SET ARITHABORT OFF 
GO
ALTER DATABASE [ProjectManager] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [ProjectManager] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [ProjectManager] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [ProjectManager] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [ProjectManager] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [ProjectManager] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [ProjectManager] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [ProjectManager] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [ProjectManager] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [ProjectManager] SET  DISABLE_BROKER 
GO
ALTER DATABASE [ProjectManager] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [ProjectManager] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [ProjectManager] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [ProjectManager] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [ProjectManager] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [ProjectManager] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [ProjectManager] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [ProjectManager] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [ProjectManager] SET  MULTI_USER 
GO
ALTER DATABASE [ProjectManager] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [ProjectManager] SET DB_CHAINING OFF 
GO
ALTER DATABASE [ProjectManager] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [ProjectManager] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [ProjectManager] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [ProjectManager] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [ProjectManager] SET QUERY_STORE = OFF
GO
USE [ProjectManager]
GO
/****** Object:  Table [dbo].[BusinessUnits]    Script Date: 29 04 2023 15:50:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BusinessUnits](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[TypesId] [int] NOT NULL,
	[CompaniesId] [int] NULL,
	[ProjectsId] [int] NULL,
	[WhiteboardsId] [int] NULL,
 CONSTRAINT [PK_BusinessUnits] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Columns]    Script Date: 29 04 2023 15:50:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Columns](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[WhiteboardsId] [int] NOT NULL,
	[Position] [int] NOT NULL,
 CONSTRAINT [PK_Columns] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK_Columns] UNIQUE NONCLUSTERED 
(
	[WhiteboardsId] ASC,
	[Position] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Invites]    Script Date: 29 04 2023 15:50:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Invites](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[State] [varchar](50) NOT NULL,
	[SenderId] [int] NOT NULL,
	[ReceiverId] [int] NOT NULL,
	[BusinessUnitsId] [int] NOT NULL,
 CONSTRAINT [PK_Invites] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK_Invites] UNIQUE NONCLUSTERED 
(
	[ReceiverId] ASC,
	[BusinessUnitsId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Notes]    Script Date: 29 04 2023 15:50:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Notes](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](500) NOT NULL,
	[ColumnsId] [int] NOT NULL,
 CONSTRAINT [PK_Notes] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Roles]    Script Date: 29 04 2023 15:50:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Roles](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Roles] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Types]    Script Date: 29 04 2023 15:50:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Types](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Types] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 29 04 2023 15:50:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Email] [nvarchar](100) NOT NULL,
	[Password] [nvarchar](200) NOT NULL,
 CONSTRAINT [PK_Users] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UQ_Users] UNIQUE NONCLUSTERED 
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UsersBusinessUnitsRoles]    Script Date: 29 04 2023 15:50:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UsersBusinessUnitsRoles](
	[UsersId] [int] NOT NULL,
	[BusinessUnitsId] [int] NOT NULL,
	[RolesId] [int] NOT NULL,
 CONSTRAINT [PK_UsersBusinessUnitsRoles] PRIMARY KEY CLUSTERED 
(
	[UsersId] ASC,
	[BusinessUnitsId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [IX_UsersBusinessUnitsRoles] UNIQUE NONCLUSTERED 
(
	[UsersId] ASC,
	[BusinessUnitsId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Whiteboards]    Script Date: 29 04 2023 15:50:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Whiteboards](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_Whiteboards] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Roles] ON 
GO
INSERT [dbo].[Roles] ([Id], [Name]) VALUES (2, N'EMPLOYEE')
GO
INSERT [dbo].[Roles] ([Id], [Name]) VALUES (3, N'MANAGER')
GO
SET IDENTITY_INSERT [dbo].[Roles] OFF
GO
SET IDENTITY_INSERT [dbo].[Types] ON 
GO
INSERT [dbo].[Types] ([Id], [Name]) VALUES (1, N'COMPANY')
GO
INSERT [dbo].[Types] ([Id], [Name]) VALUES (2, N'PROJECT')
GO
INSERT [dbo].[Types] ([Id], [Name]) VALUES (3, N'TEAM')
GO
SET IDENTITY_INSERT [dbo].[Types] OFF
GO
ALTER TABLE [dbo].[BusinessUnits]  WITH CHECK ADD  CONSTRAINT [FK_BusinessUnits_BusinessUnits] FOREIGN KEY([CompaniesId])
REFERENCES [dbo].[BusinessUnits] ([Id])
GO
ALTER TABLE [dbo].[BusinessUnits] CHECK CONSTRAINT [FK_BusinessUnits_BusinessUnits]
GO
ALTER TABLE [dbo].[BusinessUnits]  WITH CHECK ADD  CONSTRAINT [FK_BusinessUnits_BusinessUnits1] FOREIGN KEY([ProjectsId])
REFERENCES [dbo].[BusinessUnits] ([Id])
GO
ALTER TABLE [dbo].[BusinessUnits] CHECK CONSTRAINT [FK_BusinessUnits_BusinessUnits1]
GO
ALTER TABLE [dbo].[BusinessUnits]  WITH CHECK ADD  CONSTRAINT [FK_BusinessUnits_Types] FOREIGN KEY([TypesId])
REFERENCES [dbo].[Types] ([Id])
GO
ALTER TABLE [dbo].[BusinessUnits] CHECK CONSTRAINT [FK_BusinessUnits_Types]
GO
ALTER TABLE [dbo].[BusinessUnits]  WITH CHECK ADD  CONSTRAINT [FK_BusinessUnits_Whiteboards] FOREIGN KEY([WhiteboardsId])
REFERENCES [dbo].[Whiteboards] ([Id])
GO
ALTER TABLE [dbo].[BusinessUnits] CHECK CONSTRAINT [FK_BusinessUnits_Whiteboards]
GO
ALTER TABLE [dbo].[Columns]  WITH CHECK ADD  CONSTRAINT [FK_Columns_Whiteboards] FOREIGN KEY([WhiteboardsId])
REFERENCES [dbo].[Whiteboards] ([Id])
GO
ALTER TABLE [dbo].[Columns] CHECK CONSTRAINT [FK_Columns_Whiteboards]
GO
ALTER TABLE [dbo].[Invites]  WITH CHECK ADD  CONSTRAINT [FK_Invites_BusinessUnits] FOREIGN KEY([BusinessUnitsId])
REFERENCES [dbo].[BusinessUnits] ([Id])
GO
ALTER TABLE [dbo].[Invites] CHECK CONSTRAINT [FK_Invites_BusinessUnits]
GO
ALTER TABLE [dbo].[Invites]  WITH CHECK ADD  CONSTRAINT [FK_Invites_Users] FOREIGN KEY([SenderId])
REFERENCES [dbo].[Users] ([Id])
GO
ALTER TABLE [dbo].[Invites] CHECK CONSTRAINT [FK_Invites_Users]
GO
ALTER TABLE [dbo].[Invites]  WITH CHECK ADD  CONSTRAINT [FK_Invites_Users1] FOREIGN KEY([ReceiverId])
REFERENCES [dbo].[Users] ([Id])
GO
ALTER TABLE [dbo].[Invites] CHECK CONSTRAINT [FK_Invites_Users1]
GO
ALTER TABLE [dbo].[Notes]  WITH CHECK ADD  CONSTRAINT [FK_Notes_Columns] FOREIGN KEY([ColumnsId])
REFERENCES [dbo].[Columns] ([Id])
GO
ALTER TABLE [dbo].[Notes] CHECK CONSTRAINT [FK_Notes_Columns]
GO
ALTER TABLE [dbo].[UsersBusinessUnitsRoles]  WITH CHECK ADD  CONSTRAINT [FK_UsersBusinessUnits_BusinessUnits] FOREIGN KEY([BusinessUnitsId])
REFERENCES [dbo].[BusinessUnits] ([Id])
GO
ALTER TABLE [dbo].[UsersBusinessUnitsRoles] CHECK CONSTRAINT [FK_UsersBusinessUnits_BusinessUnits]
GO
ALTER TABLE [dbo].[UsersBusinessUnitsRoles]  WITH CHECK ADD  CONSTRAINT [FK_UsersBusinessUnits_Users] FOREIGN KEY([UsersId])
REFERENCES [dbo].[Users] ([Id])
GO
ALTER TABLE [dbo].[UsersBusinessUnitsRoles] CHECK CONSTRAINT [FK_UsersBusinessUnits_Users]
GO
ALTER TABLE [dbo].[UsersBusinessUnitsRoles]  WITH CHECK ADD  CONSTRAINT [FK_UsersBusinessUnitsRoles_Roles] FOREIGN KEY([RolesId])
REFERENCES [dbo].[Roles] ([Id])
GO
ALTER TABLE [dbo].[UsersBusinessUnitsRoles] CHECK CONSTRAINT [FK_UsersBusinessUnitsRoles_Roles]
GO
USE [master]
GO
ALTER DATABASE [ProjectManager] SET  READ_WRITE 
GO
