package sttawm.slick

       
 import play.api.libs.json.JsValue
 import java.time.{LocalDate, LocalTime, LocalDateTime, Duration, ZonedDateTime, OffsetDateTime}


		// AUTO-GENERATED Slick data model
		/** Stand-alone Slick data model for immediate use */
		object Tables extends {
			val profile = SlickProfile
		} with Tables

		/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
		trait Tables {
			val profile: SlickProfile
			import profile.api._
			import play.api.libs.json.JsValue
  import java.time.{LocalDate, LocalTime, LocalDateTime, Duration, ZonedDateTime, OffsetDateTime}
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = PlayEvolutions.schema ++ PlayEvolutionsLock.schema ++ Things.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema


  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[ZonedDateTime], e3: GR[Option[String]]): GR[PlayEvolutionsRow] = GR{
    prs => import prs._
    PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[ZonedDateTime], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends profile.api.Table[PlayEvolutionsRow](_tableTag, "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(hash), Rep.Some(appliedAt), applyScript, revertScript, state, lastProblem)).shaped.<>({r=>import r._; _1.map(_=> PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash SqlType(varchar), Length(255,true) */
    val hash: Rep[String] = column[String]("hash", O.Length(255,varying=true))
    /** Database column applied_at SqlType(timestamp) */
    val appliedAt: Rep[ZonedDateTime] = column[ZonedDateTime]("applied_at")
    /** Database column apply_script SqlType(text), Default(None) */
    val applyScript: Rep[Option[String]] = column[Option[String]]("apply_script", O.Default(None))
    /** Database column revert_script SqlType(text), Default(None) */
    val revertScript: Rep[Option[String]] = column[Option[String]]("revert_script", O.Default(None))
    /** Database column state SqlType(varchar), Length(255,true), Default(None) */
    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(255,varying=true), O.Default(None))
    /** Database column last_problem SqlType(text), Default(None) */
    val lastProblem: Rep[Option[String]] = column[Option[String]]("last_problem", O.Default(None))
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))


  /** GetResult implicit for fetching PlayEvolutionsLockRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsLockRow(implicit e0: GR[Int]): GR[PlayEvolutionsLockRow] = GR{
    prs => import prs._
    PlayEvolutionsLockRow(<<[Int])
  }
  /** Table description of table play_evolutions_lock. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutionsLock(_tableTag: Tag) extends profile.api.Table[PlayEvolutionsLockRow](_tableTag, "play_evolutions_lock") {
    def * = lock <> (PlayEvolutionsLockRow, PlayEvolutionsLockRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(lock)).shaped.<>(r => r.map(_=> PlayEvolutionsLockRow(r.get)), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column lock SqlType(int4), PrimaryKey */
    val lock: Rep[Int] = column[Int]("lock", O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table PlayEvolutionsLock */
  lazy val PlayEvolutionsLock = new TableQuery(tag => new PlayEvolutionsLock(tag))


  /** GetResult implicit for fetching ThingsRow objects using plain SQL queries */
  implicit def GetResultThingsRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[ThingsRow] = GR{
    prs => import prs._
    ThingsRow.tupled((<<[Int], <<?[String]))
  }
  /** Table description of table things. Objects of this class serve as prototypes for rows in queries. */
  class Things(_tableTag: Tag) extends profile.api.Table[ThingsRow](_tableTag, "things") {
    def * = (id, name) <> (ThingsRow.tupled, ThingsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), name)).shaped.<>({r=>import r._; _1.map(_=> ThingsRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(text), Default(None) */
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Default(None))
  }
  /** Collection-like TableQuery object for table Things */
  lazy val Things = new TableQuery(tag => new Things(tag))
		}
		/** Entity class storing rows of table PlayEvolutions
 *  @param id Database column id SqlType(int4), PrimaryKey
 *  @param hash Database column hash SqlType(varchar), Length(255,true)
 *  @param appliedAt Database column applied_at SqlType(timestamp)
 *  @param applyScript Database column apply_script SqlType(text), Default(None)
 *  @param revertScript Database column revert_script SqlType(text), Default(None)
 *  @param state Database column state SqlType(varchar), Length(255,true), Default(None)
 *  @param lastProblem Database column last_problem SqlType(text), Default(None) */
case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: ZonedDateTime, applyScript: Option[String] = None, revertScript: Option[String] = None, state: Option[String] = None, lastProblem: Option[String] = None)

/** Entity class storing rows of table PlayEvolutionsLock
 *  @param lock Database column lock SqlType(int4), PrimaryKey */
case class PlayEvolutionsLockRow(lock: Int)

/** Entity class storing rows of table Things
 *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
 *  @param name Database column name SqlType(text), Default(None) */
case class ThingsRow(id: Int, name: Option[String] = None)
